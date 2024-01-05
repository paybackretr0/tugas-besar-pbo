public class Faktur extends Sepatu implements BisaDibeli {
    public Faktur[] faktur;
    private String noFaktur;
    private String namaPelanggan;
    private int jumlah;

    public Faktur(String noFaktur, String namaPelanggan) {
        super(namaPelanggan);
        this.noFaktur = noFaktur;
        this.namaPelanggan = namaPelanggan;
    }

    public String getNoFaktur() {
        return noFaktur;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public int getJumlah() {
        return jumlah;
    }

    public double hitungTotal() {
        double total = 0;
        for (Sepatu item : faktur) {
            total += item.getHarga();
        }
        return total;
    }

}
