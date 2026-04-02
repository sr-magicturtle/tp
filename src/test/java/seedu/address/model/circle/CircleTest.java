package seedu.address.model.circle;

import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class CircleTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Circle(null));
    }

    @Test
    public void constructor_invalidCircleName_throwsIllegalArgumentException() {
        // empty string
        assertThrows(IllegalArgumentException.class, () -> new Circle(""));

        // invalid circle name
        assertThrows(IllegalArgumentException.class, () -> new Circle("invalid"));

        // partial valid name
        assertThrows(IllegalArgumentException.class, () -> new Circle("cli"));

        // misspelled circle name
        assertThrows(IllegalArgumentException.class, () -> new Circle("clients"));

        // numeric value
        assertThrows(IllegalArgumentException.class, () -> new Circle("123"));

        // special characters
        assertThrows(IllegalArgumentException.class, () -> new Circle("@client"));
    }

    @Test
    public void constructor_validCircleName_success() {
        // lowercase valid names
        Circle circleClient = new Circle("client");
        assert circleClient.getCircleName().equals("client");

        Circle circleProspect = new Circle("prospect");
        assert circleProspect.getCircleName().equals("prospect");

        Circle circleFriend = new Circle("friend");
        assert circleFriend.getCircleName().equals("friend");

        // uppercase valid names (should be normalized to lowercase)
        Circle circleClientUpper = new Circle("CLIENT");
        assert circleClientUpper.getCircleName().equals("client");

        // mixed case
        Circle circleProspectMixed = new Circle("PrOsPeCt");
        assert circleProspectMixed.getCircleName().equals("prospect");

        // with whitespace (should be trimmed)
        Circle circleFriendWithSpace = new Circle("  friend  ");
        assert circleFriendWithSpace.getCircleName().equals("friend");
    }

    @Test
    public void isValidCircleName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Circle.isValidCircleName(null));
    }

    @Test
    public void isValidCircleName_validNames() {
        // lowercase valid names
        assert Circle.isValidCircleName("client");
        assert Circle.isValidCircleName("prospect");
        assert Circle.isValidCircleName("friend");

        // uppercase valid names
        assert Circle.isValidCircleName("CLIENT");
        assert Circle.isValidCircleName("PROSPECT");
        assert Circle.isValidCircleName("FRIEND");

        // mixed case valid names
        assert Circle.isValidCircleName("Client");
        assert Circle.isValidCircleName("ProSpect");
        assert Circle.isValidCircleName("FrIeNd");

        // with leading/trailing whitespace
        assert Circle.isValidCircleName("  client  ");
        assert Circle.isValidCircleName("\tclient\t");
        assert Circle.isValidCircleName("\nclient\n");
    }

    @Test
    public void isValidCircleName_invalidNames() {
        // empty string
        assert !Circle.isValidCircleName("");

        // whitespace only
        assert !Circle.isValidCircleName("   ");
        assert !Circle.isValidCircleName("\t");
        assert !Circle.isValidCircleName("\n");

        // invalid circle names
        assert !Circle.isValidCircleName("invalid");
        assert !Circle.isValidCircleName("cli");
        assert !Circle.isValidCircleName("clients");
        assert !Circle.isValidCircleName("prospects");
        assert !Circle.isValidCircleName("friends");

        // numeric values
        assert !Circle.isValidCircleName("123");
        assert !Circle.isValidCircleName("1");

        // special characters
        assert !Circle.isValidCircleName("@client");
        assert !Circle.isValidCircleName("client!");
        assert !Circle.isValidCircleName("client@prospect");
        assert !Circle.isValidCircleName("client-prospect");
        assert !Circle.isValidCircleName("client/prospect");

        // partial names with spaces
        assert !Circle.isValidCircleName("client prospect");
        assert !Circle.isValidCircleName("friend client");

        // names with extra characters
        assert !Circle.isValidCircleName("clienta");
        assert !Circle.isValidCircleName("aclients");
        assert !Circle.isValidCircleName("prospect1");
    }

    @Test
    public void equals() {
        Circle client1 = new Circle("client");
        Circle client2 = new Circle("client");
        Circle clientUppercase = new Circle("CLIENT");
        Circle prospect = new Circle("prospect");

        // same object -> returns true
        assert client1.equals(client1);

        // same values (lowercase) -> returns true
        assert client1.equals(client2);

        // same values (different case) -> returns true (both normalized to lowercase)
        assert client1.equals(clientUppercase);

        // different circle names -> returns false
        assert !client1.equals(prospect);

        // different types -> returns false
        assert !client1.equals(1);
        assert !client1.equals("client");

        // null -> returns false
        assert !client1.equals(null);
    }

    @Test
    public void hashCode_consistency() {
        Circle client1 = new Circle("client");
        Circle client2 = new Circle("CLIENT");

        // same normalized circle should have same hash code
        assert client1.hashCode() == client2.hashCode();

        Circle prospect = new Circle("prospect");
        // different circles should have different hash codes (usually)
        assert client1.hashCode() != prospect.hashCode();
    }

    @Test
    public void toString_correct() {
        Circle client = new Circle("client");
        assert client.toString().equals("[client]");

        Circle prospect = new Circle("PROSPECT");
        assert prospect.toString().equals("[prospect]");

        Circle friend = new Circle("  friend  ");
        assert friend.toString().equals("[friend]");
    }

}
