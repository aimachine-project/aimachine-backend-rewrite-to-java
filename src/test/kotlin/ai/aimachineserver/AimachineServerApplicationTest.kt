package ai.aimachineserver

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import javax.naming.Context
import javax.naming.ldap.InitialLdapContext


@SpringBootTest
class AimachineServerApplicationTest {

    @Test
    fun contextLoads() {
    }

    @Test
    @Throws(Exception::class)
    fun ldapAuthenticationIsSuccessful() {
        val env: Hashtable<String, String> = Hashtable<String, String>()
        env[Context.SECURITY_AUTHENTICATION] = "simple"
        env[Context.SECURITY_PRINCIPAL] = "cn=joe,ou=users,dc=mycompany,dc=com"
        env[Context.PROVIDER_URL] = "ldap://mycompany.com:389/dc=mycompany,dc=com"
        env[Context.SECURITY_CREDENTIALS] = "joespassword"
        env[Context.INITIAL_CONTEXT_FACTORY] = "com.sun.jndi.ldap.LdapCtxFactory"
        val ctx = InitialLdapContext(env, null)
    }
}
