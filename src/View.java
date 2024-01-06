package src;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.TimeZone;

public class View {
  public static Scanner input = new Scanner(System.in);
  private static ArrayList<String> riwayatPembelian = new ArrayList<>();
  private static double totalPenjualan = 0.0;

  public static void buatTabel() {
    Config.buatTabel();
    System.out.println("Tabel Sepatu berhasil dibuat.");

  }

  public static void tambahData() {
    if (!Config.isTableSepatuExists()) {
      System.out.println("Belum ada Tabel di Database. Mohon Buat Tabel Terlebih Dahulu!");
      return;
    }

    Scanner scanner = new Scanner(System.in);

    System.out.print("Masukan ID Sepatu: ");
    String idSepatu = scanner.nextLine();

    System.out.print("Masukan Merek Sepatu: ");
    String merekSepatu = scanner.nextLine();

    System.out.print("Masukan Ukuran Sepatu: ");
    int ukuranSepatu = scanner.nextInt();

    scanner.nextLine();

    System.out.print("Masukan Jenis Sepatu: ");
    String jenisSepatu = scanner.nextLine();

    System.out.print("Masukan Warna Sepatu: ");
    String warnaSepatu = scanner.nextLine();

    System.out.print("Masukan Harga Sepatu: ");
    double harga = scanner.nextDouble();
    scanner.nextLine();

    if (Config.tambahData(idSepatu, merekSepatu, ukuranSepatu, jenisSepatu, warnaSepatu, harga)) {
      System.out.println("Data Sepatu berhasil ditambahkan!!");
      View.tampilkanData(); // Menampilkan semua data setelah menambahkan
    } else {
      System.out.println("Data Sepatu gagal ditambahkan!!");
    }

  }

  public static void tampilkanData() {
    if (Config.isTableSepatuExists()) {
      System.out.println("\n::: DATA Sepatu :::");
      System.out.println(Config.tampilkanData());
      return;
    } else {
      System.out.println("Belum ada Tabel di Database. Mohon Buat Tabel Terlebih Dahulu!");
    }
  }

  public static void hapusData() {
    if (!Config.isTableSepatuExists()) {
      System.out.println("Belum ada Tabel di Database. Mohon Buat Tabel Terlebih Dahulu!");
      return;
    }

    if (!Config.isDataSepatuAvailable()) {
      System.out.println("Data sepatu masih kosong. Tidak ada data yang bisa dihapus.");
      return;
    }
    System.out.println("\n:::DELETE DATA Sepatu :::");
    System.out.print("Masukkan ID Sepatu : ");
    String idSepatu = input.nextLine();

    if (Config.hapusData(idSepatu)) {
      System.out.println("Data Sepatu Berhasil Dihapus!!");
      tampilkanData();
    } else {
      System.out.println("Data Sepatu Gagal Dihapus!!");
    }

  }

  public static void updateData() {
    Scanner scanner = new Scanner(System.in);
    if (!Config.isTableSepatuExists()) {
      System.out.println("Belum ada Tabel di Database. Mohon Buat Tabel Terlebih Dahulu!");
      return;
    }
    if (!Config.isDataSepatuAvailable()) {
      System.out.println("Data Sepatu masih kosong. Tidak ada data Sepatu yang bisa diubah.");
      return;
    }
    System.out.println("\n::: UPDATE DATA Sepatu :::");
    System.out.print("Masukkan idSepatu : ");
    String idSepatu = scanner.nextLine();
    System.out.println("\nGanti Data Sepatu\n");
    System.out.print("Merek Sepatu (Kosongkan jika tidak ingin mengganti datanya) : ");
    String merekSepatu = scanner.nextLine();
    System.out.print("Harga Sepatu (isi 0 jika tidak ingin merubah data) : ");
    double harga = scanner.nextDouble();
    scanner.nextLine();

    if (Config.updateData(idSepatu, merekSepatu, harga)) {
      System.out.println("Data Sepatu berhasil dirubah!!");
      View.tampilkanData();
    } else {
      System.out.println("Data Sepatu gagal dirubah!!");
    }

  }

  public static void transaksiBeli() {
    Config.connection();
    Scanner scanner = new Scanner(System.in);

    try {
      if (!Config.isTableSepatuExists()) {
        System.out.println("Belum ada Tabel di Database. Mohon Buat Tabel Terlebih Dahulu!");
        return;
      }
      if (!Config.isDataSepatuAvailable()) {
        System.out.println(
            "Data Sepatu masih kosong. Transaksi tidak dapat dilakukan. Silakan Isi Stok Sepatu Terlebih Dahulu!");
        return;
      }

      System.out.print("Masukkan Nomor Faktur: ");
      String noFaktur = scanner.nextLine();
      System.out.print("Masukkan Nama Pelanggan: ");
      String namaPelanggan = scanner.nextLine();
      System.out.print("Masukkan Jumlah Sepatu yang ingin dibeli: ");
      int jumlah = scanner.nextInt();
      scanner.nextLine();

      for (int i = 0; i < jumlah; i++) {
        View.tampilkanData();
        System.out.println("\nSepatu ke-" + (i + 1));
        System.out.print("ID Sepatu: ");
        String idSepatu = scanner.nextLine();
        totalPenjualan += Config.getHarga(idSepatu);

        System.out.println();

        Date HariSekarang = new Date();
        Faktur invoice = new Faktur(noFaktur, namaPelanggan);
        SimpleDateFormat ft = new SimpleDateFormat("E dd/MM/yyyy");
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Jakarta"));

        System.out.println("============Toko Sepatu Cataleya==============");
        System.out.println("Tanggal \t\t: " + ft.format(HariSekarang));
        System.out.println("Waktu\t\t\t: " + format.format(HariSekarang) + "" + "WIB");
        System.out.println("Nomor Faktur\t\t: " + invoice.getNoFaktur());
        System.out.println("==========Data Pelanggan==========");
        System.out.println("Nama Pelanggan\t\t: " + invoice.getNamaPelanggan());
        System.out.println("============Detail Sepatu============");
        System.out.println("ID Sepatu\t\t: " + idSepatu);
        System.out.println("Merek Sepatu\t\t: " + Config.getMerekSepatu(idSepatu));
        System.out.println("Ukuran Sepatu\t\t: " + Config.getUkuran(idSepatu));
        System.out.println("Jenis Sepatu\t\t: " + Config.getJenis(idSepatu));
        System.out.println("Warna Sepatu\t\t: " + Config.getWarna(idSepatu));
        System.out.println("Harga Sepatu\t\t: " + Config.getHarga(idSepatu));
        System.out.println("===============Kasir===============");
        String kasir = "sultan minang";
        String uppercaseString = kasir.toUpperCase();
        System.out.println("Kasir\t\t\t: " + uppercaseString);

        System.out.println();

        riwayatPembelian.add("Tanggal: " + ft.format(HariSekarang) +
            ", Nomor Faktur: " + invoice.getNoFaktur() +
            ", Nama Pelanggan: " + invoice.getNamaPelanggan() +
            ", ID Sepatu: " + idSepatu +
            ", Merek: " + Config.getMerekSepatu(idSepatu) +
            ", Harga: " + Config.getHarga(idSepatu));

        // Hapus data sepatu dari database ketika sudah dibeli saat transaksi
        if (Config.hapusData(idSepatu)) {
          System.out.println("Data sepatu berhasil dihapus dari database.");
        } else {
          System.out.println("Gagal menghapus data sepatu dari database.");
        }
      }
      riwayatPembelian.add("Total Penjualan: " + totalPenjualan);
    } catch (InputMismatchException e) {
      System.out.println("Kesalahan Input, Tipe Data Tidak Sesuai");
    }

  }

  public static void tampilkanRiwayatPembelian() {
    if (!Config.isTableSepatuExists()) {
      System.out.println("Belum ada Tabel di Database. Mohon Buat Tabel Terlebih Dahulu!");
      return;
    }

    System.out.println("\n::: RIWAYAT PEMBELIAN :::");
    for (String pembelian : riwayatPembelian) {
      System.out.println(pembelian);
    }
  }
}
