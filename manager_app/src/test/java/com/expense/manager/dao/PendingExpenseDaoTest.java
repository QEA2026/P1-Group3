package com.expense.manager.dao;

import com.expense.manager.db.Database;
import com.expense.manager.models.Expense;
import com.expense.manager.models.User;

import io.opentelemetry.sdk.metrics.data.Data;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pending Expense DAO Tests")
public class PendingExpenseDaoTest {
    @Mock
    Database db;
    @Mock
    Connection connection;
    @Mock
    PreparedStatement ps;
    @InjectMocks
    ExpenseDao expenseDao;

    @Test
    @DisplayName("findByID with valid expense")
    void findById_valid_returnsExpense() throws SQLException {

    }

    @Test
    @DisplayName("findByID with no expenses matching ID")
    void findById_invalid_returnsNull() {

    }

    @Test
    @DisplayName("findAll gets all current expenses")
    void findAll_valid_returnExpenses() {

    }

    @Test
    @DisplayName("findAll returns empty list with no expenses")
    void findAll_noExpenses_returnsEmptyList() {

    }

    @Test
    @DisplayName("findByStatus gets expenses with matching status")
    void findByStatus_matchingExists_returnsMatching() {

    }

    @Test
    @DisplayName("findByUserId gets expenses with matching user ID")
    void findByUserId_userHasExpenses_returnsMatching() {

    }

    @Test
    @DisplayName("findByDate gets expenses with matching date")
    void findByDate_expensesOnDate_returnsMatching() {

    }
}
