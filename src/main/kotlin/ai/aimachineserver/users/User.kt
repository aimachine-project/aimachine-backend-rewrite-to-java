package ai.aimachineserver.users

import lombok.AccessLevel
import lombok.Data
import lombok.NoArgsConstructor
import lombok.RequiredArgsConstructor
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
class User(
    private val username: String,
    private val password: String,
    private val role: String = UserRole.USER.roleName
) : UserDetails {
    constructor() : this("", "", "")

    private companion object {
        const val serialVersionUID = 1L
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null

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
