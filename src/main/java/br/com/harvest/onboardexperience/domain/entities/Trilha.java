package br.com.harvest.onboardexperience.domain.entities;

import lombok.*;

import javax.persistence.*;

@Data
@Entity()
@Table(name = "tb_trilha",schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Trilha extends BaseEntityAudit{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trilha")
    private Long id;

    @Column(name = "descricao_trilha")
    private String descricaoTrilha;


    @Column(name = "nome_trilha")
    private String nomeTrilha;

    @Column(name = "arquivo_trilha")
    private byte[] arquivoTrilhaBytes;


    @Column(name = "arquivo_trilha_nome")
    private String arquivoTrilhaNome;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userCreatedTrilha;

}
