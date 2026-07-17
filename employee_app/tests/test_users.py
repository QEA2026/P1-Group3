import unittest
from unittest.mock import patch, MagicMock

from controllers import users


class TestLogin(unittest.TestCase):

    @patch("controllers.users.get_connection")
    def test_login_valid_credentials_returns_user(self, mock_get_connection):
        fake_row = (1, "alice", "password123", "Employee")
        mock_conn = MagicMock()
        mock_conn.execute.return_value.fetchone.return_value = fake_row
        mock_get_connection.return_value = mock_conn

