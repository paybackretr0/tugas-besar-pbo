package src;

class Sepatu {
    private String idSepatu;
    private String merekSepatu;
    private int ukuranSepatu;
    private String jenisSepatu;
    private String warnaSepatu;
    private double harga;

    public Sepatu(String idSepatu, String merekSepatu, int ukuranSepatu, String jenisSepatu, String warnaSepatu,
            double harga) {
        this.idSepatu = idSepatu;
        this.merekSepatu = merekSepatu;
        this.ukuranSepatu = ukuranSepatu;
        this.jenisSepatu = jenisSepatu;
        this.warnaSepatu = warnaSepatu;
        this.harga = harga;
    }

    public Sepatu(String idSepatu) {
        this.idSepatu = idSepatu;
    }

    public String getId() {
        return idSepatu;
    }

    public String getMerek() {
        return merekSepatu;
    }

    public int getUkuran() {
        return ukuranSepatu;
    }

    public String getJenis() {
        return jenisSepatu;
    }

    public String getWarna() {
        return warnaSepatu;
    }

    public double getHarga() {
        return harga;
    }
}
