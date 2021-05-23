package ai.aimachineserver.userlogin

import lombok.AccessLevel
import lombok.Data
import lombok.NoArgsConstructor
import lombok.RequiredArgsConstructor
import org.hibernate.annotations.Type
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import javax.persistence.*

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
class User(
    private val username: String,
    private val password: String
) : UserDetails {
    constructor() : this("", "")

    private companion object {
        const val serialVersionUID = 1L
        const val DEFAULT_ROLE_NAME = "ROLE_USER"
    }

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    val id: UUID? = null

    private val role = DEFAULT_ROLE_NAME

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(role))
    }

    override fun getPassword(): String = password
    override fun getUsername() = username
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}
