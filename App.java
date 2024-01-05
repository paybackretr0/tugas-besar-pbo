import java.util.Scanner;
import java.util.InputMismatchException;

public class App {
    private static String username = "admin";
    private static String password = "admin";

    public static void main(String[] args) {
        Config.connection();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Selamat datang! Silakan login.");

        System.out.print("Masukkan username: ");
        String inputUsername = scanner.nextLine();
        System.out.print("Masukkan password: ");
        String inputPassword = scanner.nextLine();

        String captcha = CaptchaGenerator.generateCaptcha(5);

        System.out.println("CAPTCHA: " + captcha);
        System.out.print("Masukkan CAPTCHA: ");
        String inputCaptcha = scanner.nextLine();

        if (checkCaptcha(inputCaptcha, captcha) && checkLogin(inputUsername, inputPassword, username, password)) {
            System.out.println("Login berhasil!");

            try {

                while (true) {

                    System.out.print("\n====== MENU ======\n"
                            + "1. Buat Table Sepatu\n"
                            + "2. Tambah Data Sepatu\n"
                            + "3. Lihat Data Sepatu\n"
                            + "4. Hapus Data Sepatu\n"
                            + "5. Update Data Sepatu\n"
                            + "6. Transaksi Pembelian Sepatu\n"
                            + "7. Riwayat Transaksi\n"
                            + "0. Exit\n"
                            + "Pilih[1/2/3/4/5/6/0] : ");

                    String pilihan = scanner.nextLine().trim();

                    if (pilihan.equalsIgnoreCase("0")) {
                        System.out.println("Terima kasih");
                        break;
                    }

                    switch (pilihan) {
                        case "1":
                            try {

                                View.buatTabel();

                            } catch (Exception e) {
                                System.err.println(
                                        "Input tidak valid. Pastikan input sesuai dengan tipe data yang diminta.");
                            }

                            break;
                        case "2":
                            View.tambahData();
                            break;
                        case "3":
                            View.tampilkanData();
                            break;
                        case "4":
                            View.hapusData();
                            break;
                        case "5":
                            View.updateData();
                            break;
                        case "6":
                            View.transaksiBeli();
                            break;
                        case "7":
                            View.tampilkanRiwayatPembelian();
                            break;
                        default:
                            System.out.println("Pilihan Tidak Benar!!");
                            break;

                    }

                }
            } catch (InputMismatchException e) {
                System.out.println("Kesalahan Input, Tipe Data tidak sesuai!");
            }
        } else {
            System.out.println("Login gagal. Coba lagi.");
        }
        scanner.close();
    }

    public static boolean checkLogin(String inputUsername, String inputPassword,
            String username, String password) {
        return username.equals(inputUsername) && password.equals(inputPassword);
    }

    public static boolean checkCaptcha(String inputCaptcha, String captcha) {
        return captcha.equalsIgnoreCase(inputCaptcha);
    }

}