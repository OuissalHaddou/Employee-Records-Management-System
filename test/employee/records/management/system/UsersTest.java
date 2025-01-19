package employee.records.management.system;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class UsersTest {

    @InjectMocks
    private Users users;

    @Mock
    private Connexion mockConnexion;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.initMocks(this);
        when(mockConnexion.getConnection()).thenReturn(mockConnection);
        users = new Users(); // Create a new instance of Users
    }

    /**
     * Test of actionPerformed method, of class Users.
     */
    @Test
    public void testActionPerformedCreateUser() throws Exception {
        // Simulate user input
        users.tfUserID.setText("1");
        users.tfUsername.setText("testuser");
        users.tfPassword.setText("password");
        users.tfRole.setText("Admin");
        users.tfDepartment.setText("IT");

        // Mock database behavior
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Simulate clicking the "Create" button
        ActionEvent ae = new ActionEvent(users.btnCreate, ActionEvent.ACTION_PERFORMED, null);
        users.actionPerformed(ae);

        // Verify the interaction with the database
        verify(mockPreparedStatement).executeUpdate();
    }

    /**
     * Test of main method, of class Users.
     */
    @Test
    public void testMain() {
        String[] args = {};
        Users.main(args);
        // This test ensures that the main method runs without errors.
        assertTrue(true); // Assuming main method should run without exceptions
    }
}
