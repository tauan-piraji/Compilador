package compilador;

public class Simbolo {

    private String nome;
    private String Categoria;
    private String tipo;
    private Boolean nivel;

    public Simbolo(String nome, String categoria, String tipo, Boolean nivel) {
        this.nome = nome;
        this.Categoria = categoria;
        this.tipo = tipo;
        this.nivel = nivel;
    }

    public Simbolo() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getNivel() {
        return nivel;
    }

    public void setNivel(Boolean nivel) {
        this.nivel = nivel;
    }
}
