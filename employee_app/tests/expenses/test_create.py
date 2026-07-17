from unittest.mock import Mock, patch
from controllers.expenses import create
from models.expenses import Expense

class TestCreate:
    @patch("controllers.expenses.get_connection")
    def test_create_validShouldReturnApprovals(self, mock_get_connection):
        conn = Mock()
        cursor = Mock()

        mock_get_connection.return_value = conn
        conn.execute.return_value = cursor
        cursor.lastrowid = 5

        expense = Expense(1, 10, "description", "may15")
        resulting_expense = create(expense)

        assert resulting_expense.__str__() == (f"Expense #5: $10 for description "
        f"on may15")




