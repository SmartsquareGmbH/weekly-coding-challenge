import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


public class ProjektTest {
    Projekt projekt = new Projekt();

    @Test
    public void findOnePassword() throws Exception {
        int start = 357253;
        int end = 357777;

        int result = projekt.findPasswordInRange(start, end);

        assertThat(result).isEqualTo(1);
    }

    @Test
    public void findFiveDigitPasswordShouldThrow() throws Exception {
        int start = 35725;
        int end = 35777;

        assertThatThrownBy(()->{
            projekt.findPasswordInRange(start, end);
        }).hasMessage("Not in range");
    }

    @Test
    public void findSevenDigitPasswordShouldThrow() throws Exception {
        int start = 3572577;
        int end = 3577777;

        assertThatThrownBy(()->{
            projekt.findPasswordInRange(start, end);
        }).hasMessage("Not in range");
    }

    @Test
    public void findAllPasswords() throws Exception {
        int start = 357253;
        int end = 357778;

        int result = projekt.findPasswordInRange(start, end);

        assertThat(result).isEqualTo(2);
    }

    @Test
    public void findAllPasswordsInRange() throws Exception {
        int start = 357253;
        int end = 892942;

        int result = projekt.findPasswordInRange(start, end);

        assertThat(result).isEqualTo(530);
    }

    @Test
    public void findAllPasswordsWithOnly2Doubles() throws Exception {
        int start = 357253;
        int end = 892942;

        int result = projekt.findPasswordInRange(start, end);

        assertThat(result).isNotNegative();
    }

}
