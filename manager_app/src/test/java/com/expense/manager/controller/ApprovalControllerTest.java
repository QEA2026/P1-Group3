package com.expense.manager.controller;

import com.expense.manager.dao.ApprovalDao;
import com.expense.manager.models.Approval;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Testing for the ApprovalController")
public class ApprovalControllerTest {

    @Mock
    ApprovalDao dao;

    @InjectMocks
    ApprovalController approvalController;


    @Test
    @DisplayName("Update Approval - Approve Expense")
    void updateApproval_approveExpense() throws SQLException {


        Approval approval = new Approval(
                1,
                10,
                "approved",
                5,
                "Approved for client travel.",
                "2026-07-23"
        );


        approvalController.updateApproval(10, approval);


        verify(dao).updateStatus(
                10,
                "approved",
                5,
                "Approved for client travel."
        );
    }


    @Test
    @DisplayName("Update Approval - Reject Expense")
    void updateApproval_rejectExpense() throws SQLException {


        Approval approval = new Approval(
                2,
                11,
                "denied",
                5,
                "Receipt was missing.",
                "2026-07-23"
        );


        approvalController.updateApproval(11, approval);

        verify(dao).updateStatus(
                11,
                "denied",
                5,
                "Receipt was missing."
        );
    }
}
