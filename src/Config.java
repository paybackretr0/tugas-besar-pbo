package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;

public class Config {

  private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  private static final String DB_URL = "jdbc:mysql://localhost:3306/dbsepatu";
  private static final String USER = "root";
  private static final String PASS = "";

  private static Connection connect;
  private static Statement statement;
  private static ResultSet resultData;

  public static void connection()

  {
    try {
      Class.forName(JDBC_DRIVER);
      connect = DriverManager.getConnection(DB_URL, USER, PASS);
      // System.out.println("Koneksi Database Berhasil");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static boolean isTableSepatuExists() {

    boolean tableExists = false;
    try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
      DatabaseMetaData meta = connection.getMetaData();
      try (ResultSet tables = meta.getTables(null, null, "sepatu", null)) {
        tableExists = tables.next();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return tableExists;
  }

  public static void buatTabel() {
    connection();

    try {
      statement = connect.createStatement();

      // Membuat query untuk menciptakan tabel sepatu
      String query = "CREATE TABLE IF NOT EXISTS sepatu (" +
          "idSepatu VARCHAR(50) PRIMARY KEY," + // Misalnya idSepatu sebagai primary key
          "merekSepatu VARCHAR(100) NOT NULL," +
          "ukuranSepatu INT(15)NOT NULL," +
          "jenisSepatu VARCHAR(100) NOT NULL," +
          "warnaSepatu VARCHAR(100) NOT NULL," +
          "harga DOUBLE NOT NULL" + ")";

      // Eksekusi query untuk menciptakan tabel
      statement.execute(query);

    } catch (SQLException e) {
      System.out.println("Error saat menciptakan tabel: " + e.getMessage());
    } finally {
      try {
        if (statement != null) {
          statement.close();
        }
        if (connect != null) {
          connect.close();
        }
      } catch (SQLException e) {
        System.out.println("Error saat menutup statement atau koneksi: " + e.getMessage());
      }
    }
  }

  public static boolean isDataSepatuAvailable() {
    connection();
    boolean dataAvailable = false;

    try {
      statement = connect.createStatement();
      String query = "SELECT COUNT(*) AS count FROM sepatu";
      resultData = statement.executeQuery(query);

      if (resultData.next()) {
        int count = resultData.getInt("count");
        if (count > 0) {
          dataAvailable = true;
        }
      }
      statement.close();
      connect.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return dataAvailable;
  }

  public static String tampilkanData() {
    Config.connection();

    String data = "Maaf data tidak ada";

    try {

      // buat object statement yang ambil dari koneksi
      statement = connect.createStatement();

      // query select all data from database
      String query = "SELECT idSepatu, merekSepatu, ukuranSepatu, jenisSepatu, warnaSepatu, harga FROM sepatu";

      // eksekusi query-nya
      resultData = statement.executeQuery(query);

      // set variabel data jadi null
      data = "";

      // looping pengisian variabel data
      while (resultData.next()) {
        data += "idSepatu : " + resultData.getString("idSepatu") + "| merekSepatu : "
            + resultData.getString("merekSepatu") + "| ukuranSepatu : " + resultData.getInt("ukuranSepatu")
            + "| jenisSepatu : "
            + resultData.getString("jenisSepatu") + "| warnaSepatu : " + resultData.getString("warnaSepatu")
            + "| harga : "
            + resultData.getDouble("harga") + "\n";
      }
      // close statement dan connection
      statement.close();
      connect.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

    return data;

  }

  public static boolean tambahData(String idSepatu, String merekSepatu, int ukuranSepatu, String jenisSepatu,
      String warnaSepatu, double harga) {
    connection();
    boolean data = false;

    try {

      statement = connect.createStatement();

      String query = "INSERT INTO sepatu VALUES ('" + idSepatu + "', '" + merekSepatu + "', '" + ukuranSepatu + "', '"
          + jenisSepatu + "', '" + warnaSepatu + "', " + harga + ")";

      if (!statement.execute(query)) {
        data = true;
      }

      // close statement dan koneksi
      statement.close();
      connect.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

    return data;
  }

  public static boolean hapusData(String idSepatu) {
    connection();
    boolean success = false;

    try {
      statement = connect.createStatement();

      String query = "DELETE FROM sepatu WHERE idSepatu = '" + idSepatu + "'";

      int rowsAffected = statement.executeUpdate(query);
      if (rowsAffected > 0) {
        success = true; // Data berhasil dihapus jika baris terpengaruh lebih dari 0
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return success;
  }

  public static boolean updateData(String idSepatu, String merekSepatu, double harga) {
    connection();
    boolean data = false;

    try {
      PreparedStatement preparedStatement = connect
          .prepareStatement("SELECT merekSepatu, harga FROM sepatu WHERE idSepatu = ?");
      preparedStatement.setString(1, idSepatu);

      ResultSet resultData = preparedStatement.executeQuery();
      String itemCheck = "";
      double hargaCek = 0;

      while (resultData.next()) {
        itemCheck = resultData.getString("merekSepatu");
        hargaCek = resultData.getDouble("harga");
      }

      if (!merekSepatu.isEmpty()) {
        itemCheck = merekSepatu;
      }
      if (harga != 0) {
        hargaCek = harga;
      }

      PreparedStatement updateStatement = connect
          .prepareStatement("UPDATE sepatu SET merekSepatu = ?, harga = ? WHERE idSepatu = ?");
      updateStatement.setString(1, itemCheck);
      updateStatement.setDouble(2, hargaCek);
      updateStatement.setString(3, idSepatu);

      int rowsAffected = updateStatement.executeUpdate();
      if (rowsAffected > 0) {
        data = true;
      }

      preparedStatement.close();
      updateStatement.close();
      connect.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return data;
  }

  public static String selectData(String idSepatu) {
    connection();
    String data = "Maaf, data tidak ditemukan";

    try {
      statement = connect.createStatement();

      // Query untuk mengambil data sepatu berdasarkan ID
      String query = "SELECT * FROM sepatu WHERE idSepatu = '" + idSepatu + "'";

      // Eksekusi query
      resultData = statement.executeQuery(query);

      // Reset variabel data
      data = "";

      // Jika data ditemukan, tampilkan informasi sepatu
      if (resultData.next()) {
        data = "ID Sepatu: " + resultData.getString("idSepatu") +
            " | Merek: " + resultData.getString("merekSepatu") +
            " | Ukuran: " + resultData.getInt("ukuranSepatu") +
            " | Jenis: " + resultData.getString("jenisSepatu") +
            " | Warna: " + resultData.getString("warnaSepatu") +
            " | Harga: " + resultData.getDouble("harga");
      } else {
        data = "Sepatu dengan ID " + idSepatu + " tidak ditemukan.";
      }

      // Tutup statement dan koneksi
      statement.close();
      connect.close();

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return data;
  }

  public static String getMerekSepatu(String idSepatu) {
    connection();
    String merekSepatu = "Merek Sepatu tidak Ditemukan";

    try {
      statement = connect.createStatement();

      // Query untuk mengambil merek sepatu berdasarkan ID
      String query = "SELECT merekSepatu FROM sepatu WHERE idSepatu = '" + idSepatu + "'";

      // Eksekusi query
      resultData = statement.executeQuery(query);

      // Reset variabel merekSepatu
      merekSepatu = "";

      // Jika data ditemukan, ambil merek sepatu
      if (resultData.next()) {
        merekSepatu = resultData.getString("merekSepatu");
      } else {
        merekSepatu = "Merek sepatu dengan ID " + idSepatu + " tidak ditemukan.";
      }

      // Tutup statement dan koneksi
      statement.close();
      connect.close();

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return merekSepatu;
  }

  public static int getUkuran(String idSepatu) {
    connection();
    int ukuranSepatu = 0;

    try {
      statement = connect.createStatement();

      // Query untuk mengambil ukuran sepatu berdasarkan ID
      String query = "SELECT ukuranSepatu FROM sepatu WHERE idSepatu = '" + idSepatu + "'";

      // Eksekusi query
      resultData = statement.executeQuery(query);

      // Reset variabel ukuranSepatu
      ukuranSepatu = 0;

      // Jika data ditemukan, ambil ukuran sepatu
      if (resultData.next()) {
        ukuranSepatu = resultData.getInt("ukuranSepatu");
      } else {
        System.out.println("Ukuran Sepatu  dengan ID " + idSepatu + " tidak ditemukan.");
      }

      // Tutup statement dan koneksi
      statement.close();
      connect.close();

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return ukuranSepatu;
  }

  public static String getJenis(String idSepatu) {
    connection();
    String jenisSepatu = "Jenis Sepatu tidak Ditemukan";

    try {
      statement = connect.createStatement();

      // Query untuk mengambil Jenis sepatu berdasarkan ID
      String query = "SELECT jenisSepatu FROM sepatu WHERE idSepatu = '" + idSepatu + "'";

      // Eksekusi query
      resultData = statement.executeQuery(query);

      // Reset variabel JenisSepatu
      jenisSepatu = "";

      // Jika data ditemukan, ambil Jenis sepatu
      if (resultData.next()) {
        jenisSepatu = resultData.getString("jenisSepatu");
      } else {
        jenisSepatu = "Jenis sepatu dengan ID " + idSepatu + " tidak ditemukan.";
      }

      // Tutup statement dan koneksi
      statement.close();
      connect.close();

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return jenisSepatu;

  }

  public static String getWarna(String idSepatu) {
    connection();
    String warnaSepatu = "warna Sepatu tidak Ditemukan";

    try {
      statement = connect.createStatement();

      // Query untuk mengambil warna sepatu berdasarkan ID
      String query = "SELECT warnaSepatu FROM sepatu WHERE idSepatu = '" + idSepatu + "'";

      // Eksekusi query
      resultData = statement.executeQuery(query);

      // Reset variabel warnaSepatu
      warnaSepatu = "";

      // Jika data ditemukan, ambil warna sepatu
      if (resultData.next()) {
        warnaSepatu = resultData.getString("warnaSepatu");
      } else {
        warnaSepatu = "Warna sepatu dengan ID " + idSepatu + " tidak ditemukan.";
      }

      // Tutup statement dan koneksi
      statement.close();
      connect.close();

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return warnaSepatu;

  }

  public static double getHarga(String idSepatu) {
    connection();
    double hargaSepatu = 0;

    try {
      statement = connect.createStatement();

      // Query untuk mengambil harga sepatu berdasarkan ID
      String query = "SELECT harga FROM sepatu WHERE idSepatu = '" + idSepatu + "'";

      // Eksekusi query
      resultData = statement.executeQuery(query);

      // Reset variabel hargaSepatu
      hargaSepatu = 0;

      // Jika data ditemukan, ambil harga sepatu
      if (resultData.next()) {
        hargaSepatu = resultData.getDouble("harga");
      } else {
        System.out.println("Harga Sepatu dengan ID " + idSepatu + " tidak ditemukan.");
      }

      // Tutup statement dan koneksi
      statement.close();
      connect.close();

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return hargaSepatu;
  }
}
