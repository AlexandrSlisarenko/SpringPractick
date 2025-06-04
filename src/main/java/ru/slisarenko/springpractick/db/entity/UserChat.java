package ru.slisarenko.springpractick.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users_chat", schema = "data_jpa")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserChat  implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    public void setUser(User user){
        this.user = user;
        this.user.getChats().add(this);
    }

    public void setChat(Chat chat){
        this.chat = chat;
        this.chat.getChats().add(this);
    }
}
