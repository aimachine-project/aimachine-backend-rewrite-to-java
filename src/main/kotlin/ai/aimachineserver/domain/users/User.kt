package ai.aimachineserver.domain.users

import lombok.AccessLevel
import lombok.Data
import lombok.NoArgsConstructor
import lombok.RequiredArgsConstructor
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
class User(
    private var username: String?,
    private var encodedPassword: String
) : UserDetails {
    constructor() : this("", "")

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private val id: Long? = null

    companion object {
        private const val serialVersionUID = 1L
    }

    override fun getAuthorities(): List<SimpleGrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_USER"))
    }

    override fun getUsername() = username

    override fun getPassword() = encodedPassword

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}
