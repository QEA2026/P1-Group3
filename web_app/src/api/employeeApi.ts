import type {
  User,
  Expense,
  Approval,
} from '../types/models'

const BASE_URL = 'http://localhost:8080'

async function request<T>(
  endpoint: string,
  options?: RequestInit
): Promise<T> {
  const response = await fetch(
    `${BASE_URL}${endpoint}`,
    {
      headers: {
        'Content-Type': 'application/json',
        Accept: 'application/json',
      },
      ...options,
    }
  )

  if (!response.ok) {
    const errorText =
      await response.text()

    throw new Error(
      `HTTP ${response.status}: ${errorText}`
    )
  }

  if (response.status === 204) {
    return undefined as T
  }

  return response.json()
}

export const employeeApi = {
  // -------------------------
  // Authentication
  // -------------------------

  async login(
    username: string,
    password: string
  ): Promise<User> {
    return request<User>(
      '/users/login',
      {
        method: 'POST',
        body: JSON.stringify({
          username,
          password,
        }),
      }
    )
  },

  // -------------------------
  // Expenses
  // -------------------------

  async getExpenseById(
    expenseId: number
  ): Promise<Expense> {
    return request<Expense>(
      `/expenses/${expenseId}`
    )
  },

  async getMyExpenses(
    userId: number
  ): Promise<Expense[]> {
    return request<Expense[]>(
      `/expenses/user/${userId}`
    )
  },

  async getMyExpenseHistory(
    userId: number
  ): Promise<Expense[]> {
    return request<Expense[]>(
      `/expenses/user/${userId}/history`
    )
  },

  async createExpense(
    expense: Omit<Expense, 'id'>
  ): Promise<Expense> {
    return request<Expense>(
      '/expenses',
      {
        method: 'POST',
        body: JSON.stringify(expense),
      }
    )
  },

  async updateExpense(
    expenseId: number,
    expense: Omit<Expense, 'id'>
  ): Promise<void> {
    await request<void>(
      `/expenses/${expenseId}`,
      {
        method: 'PUT',
        body: JSON.stringify(expense),
      }
    )
  },

  async deleteExpense(
    expenseId: number
  ): Promise<void> {
    await request<void>(
      `/expenses/${expenseId}`,
      {
        method: 'DELETE',
      }
    )
  },

  // -------------------------
  // Approvals
  // -------------------------

  async getApprovalByExpenseId(
    expenseId: number
  ): Promise<Approval> {
    return request<Approval>(
      `/approvals/expense/${expenseId}`
    )
  },

  async createApproval(
    approval: Omit<Approval, 'id'>
  ): Promise<Approval> {
    return request<Approval>(
      '/approvals',
      {
        method: 'POST',
        body: JSON.stringify(approval),
      }
    )
  },
}