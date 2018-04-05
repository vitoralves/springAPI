package br.com.spako.api.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "produto")
public class Produto implements Serializable {

    private static final long serialVersionUID = 475918891428093041L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String nome;
    @Column
    private BigDecimal valor;
    @Column
    private String descricao;
    @Column
    private BigDecimal desconto;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "img1")
    private byte[] img1;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "img2")
    private byte[] img2;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "img3")
    private byte[] img3;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "img4")
    private byte[] img4;
    @Column
    private int qnt;
    @JoinColumn(name = "categoria", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Categoria categoria;
    @JoinColumn(name = "fornecedor", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UsuarioFornecedor fornecedor;

    public Produto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public byte[] getImg1() {
        return img1;
    }

    public void setImg1(byte[] img1) {
        this.img1 = img1;
    }

    public byte[] getImg2() {
        return img2;
    }

    public void setImg2(byte[] img2) {
        this.img2 = img2;
    }

    public byte[] getImg3() {
        return img3;
    }

    public void setImg3(byte[] img3) {
        this.img3 = img3;
    }

    public byte[] getImg4() {
        return img4;
    }

    public void setImg4(byte[] img4) {
        this.img4 = img4;
    }

    public int getQnt() {
        return qnt;
    }

    public void setQnt(int qnt) {
        this.qnt = qnt;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public UsuarioFornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(UsuarioFornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Produto other = (Produto) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
