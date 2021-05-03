package ai.aimachineserver.userlogin

import com.fasterxml.jackson.annotation.JsonIgnore
import lombok.AccessLevel
import lombok.Data
import lombok.NoArgsConstructor
import lombok.RequiredArgsConstructor
import java.io.Serializable
import javax.persistence.*

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
class User(
    val username: String,
    val password: String
) : Serializable {
    constructor() : this("", "")

    companion object {
        private const val serialVersionUID = 1L
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    val id: Long? = null
}
