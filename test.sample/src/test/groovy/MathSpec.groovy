import spock.lang.Specification
import spock.lang.Unroll

class MathSpec extends Specification {

    @Unroll
    def "maximum between #a and #b is #c"() {
        expect:
        Math.max(a,b) == c

        where:
        a | b || c
        1 | 2 || 2
        4 | 2 || 2
        0 | 0 || 0
    }
}
