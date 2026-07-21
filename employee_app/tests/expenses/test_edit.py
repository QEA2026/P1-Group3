from unittest.mock import MagicMock, patch

import pytest
from models.expenses import Expense
from controllers.expenses import edit
import sqlite3
from utils.exceptions.database_exception import DatabaseException

class TestEdit:
    @patch("controllers.expenses.get_connection")
    def test_edit_validShouldReturnApproval(self, mock_get_connection):
        conn = MagicMock()
        mock_get_connection.return_value.__enter__.return_value = conn
        cursor = MagicMock()
        conn.execute.return_value = cursor
        cursor.rowcount = 1

        expense = Expense(1, 10, "new expense", "new date", id=5)
        edited_expense = edit(expense)

        assert edited_expense.user_id == 1
        assert edited_expense.amount == 10
        assert edited_expense.description == "new expense"
        assert edited_expense.date == "new date"
        assert edited_expense.id == 5

    @patch("controllers.expenses.get_connection")
    def test_edit_invalidShouldRaiseDatabaseError(self, mock_get_connection):
        mock_get_connection.side_effect = sqlite3.IntegrityError

        expense = Expense(1, 1, "description", "", 1)

        with pytest.raises(DatabaseException) as e:
            edit(expense)

        assert str(e.value) == "Contraints violated"

    @patch("controllers.expenses.get_connection")
    def test_edit_failedConnectionShouldRaiseDatabaseError(self, mock_get_connection):
        mock_get_connection.side_effect = sqlite3.DatabaseError

        expense = Expense(1, 1, "description", "date", 1)

        with pytest.raises(DatabaseException) as e:
            edit(expense)

        assert str(e.value) == "Database execution failed"