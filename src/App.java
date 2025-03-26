
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class App {
    public static void main(String[] args) throws Exception {
        Connection conexion  = getConnction();

        // buscarClientes(conexion);
        // getProductosParaReponer(conexion, 20);
        getProductosGama(conexion, "Aromáticas");

        cerrarConexion(conexion);

    }

    private static Connection getConnction() {
        String host = "127.0.0.1"; // localhost
        String port = "3306"; // por defecto es el puerto que utiliza
        String name = "root"; // usuario de la base de datos
        String password = "root"; // password de la base de datos
        String database = "vivero"; // nombre de la base de datos recien creada, en este caso vivero.
        // Esto especifica una zona horaria, no es obligatorio de utilizar, pero en
        // algunas zonas genera conflictos de conexión si no existiera
        String zona = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + zona;
        // esto indica la ruta de conexion, que es la combinacion de
        // host,usuario,puerto, nombre de la base de datos a la cual queremos
        // conectarnos y la zona horaria (si se precisara).

        Connection conexion = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            conexion = DriverManager.getConnection(url, name, password);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el conector JDBC: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
        return conexion;



    }

    private static void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("La conexión a la base de datos fue cerrada de manera exitosa");
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión:" + e.getMessage());
            }
        }    
    }



    public static void buscarClientes(Connection conexion) {
        String sql = "SELECT nombre_contacto, apellido_contacto, telefono FROM cliente";
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int count = 0;
            while (rs.next()) {
                    String nombre = rs.getString("nombre_contacto");
                    String apellido = rs.getString("apellido_contacto");
                    String telefono = rs.getString("telefono");
                    count++;
                    System.out.println(count + " - " + nombre + " " + apellido + " -  "+ telefono);
            }
            // Cerrar ResultSet y Statement dentro del bloque try-catch-finally
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error en la consulta: " + e.getMessage());
        }
    }



    public static void getProductosParaReponer(Connection conexion, int puntoReposicion) {
        String sql = "SELECT nombre, cantidad_en_stock FROM producto WHERE cantidad_en_stock < " + puntoReposicion;
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int count = 0;
            while (rs.next()) {
                    String nombre = rs.getString("nombre");
                    int stock = rs.getInt("cantidad_en_stock");
                    count++;
                    System.out.println(count + " - " + nombre + " -  "+ stock);
            }
            // Cerrar ResultSet y Statement dentro del bloque try-catch-finally
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error en la consulta: " + e.getMessage());
        }
    }



    public static void getProductosGama(Connection conexion, String nombreGama) {
        String sql = "Select p.nombre, p.codigo_producto, ga.gama, ga.id_gama from producto as p Join gama_producto as ga On p.id_gama = ga.id_gama Where ga.gama = '" + nombreGama + "'";
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int count = 0;
            while (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String codigoProducto = rs.getString("codigo_producto");
                    String gama = rs.getString("gama");
                    int codigoGama = rs.getInt("id_gama");

                    count++;
                    System.out.println(count + " - " + nombre + " -  "+ codigoProducto + " - " + gama + " - " + codigoGama);
            }
            // Cerrar ResultSet y Statement dentro del bloque try-catch-finally
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error en la consulta: " + e.getMessage());
        }
    }

}

