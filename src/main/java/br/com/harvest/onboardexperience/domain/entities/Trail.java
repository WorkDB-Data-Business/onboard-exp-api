package br.com.harvest.onboardexperience.domain.entities;

import lombok.*;

import javax.persistence.*;

@Data
@Entity()
@Table(name = "tb_trail",schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Trail extends BaseEntityAudit{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trail")
    private Long id;

    @Column(name = "name_trail")
    private String nameTrail;

    @Column(name = "description_trail")
    private String descriptionTrail;

    @Column(name = "arquivo_trilha")
    private byte[] arquivoTrilhaBytes;

    @Column(name = "arquivo_trilha_nome")
    private String arquivoTrilhaNome;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userCreatedTrail;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_available")
    private Boolean isAvailable;


}
