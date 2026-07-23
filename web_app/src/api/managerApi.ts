import type {
  User,
  Expense,
  Approval,
} from '../types/models'

const BASE_URL = 'http://localhost:9090'

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

  if (
    response.status === 204 ||
    response.status === 200 &&
    !(await response.clone().text())
  ) {
    return undefined as T
  }

  return response.json()
}

export const managerApi = {
  // ====================
  // Authentication
  // ====================

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

  // ====================
  // Expenses
  // ====================

  async findAllExpenses(): Promise<Expense[]> {
    return request<Expense[]>(
      '/expenses'
    )
  },

  async findExpenseById(
    expenseId: number
  ): Promise<Expense> {
    return request<Expense>(
      `/expenses/${expenseId}`
    )
  },

  // ====================
  // Approvals
  // ====================

  async findAllApprovals(): Promise<Approval[]> {
    return request<Approval[]>(
      '/approvals'
    )
  },

    async getApprovalByExpenseId(
        expenseId: number
        ): Promise<Approval | null> {
        const approvals =
            await request<Approval[]>('/approvals')

        return (
            approvals.find(
            (approval) =>
                approval.expense_id === expenseId
            ) ?? null
        )
    },

  async updateApprovalStatus(
    expenseId: number,
    status: string,
    reviewerId: number,
    comment: string
  ): Promise<void> {
    return request<void>(
      `/approvals/${expenseId}`,
      {
        method: 'PUT',
        body: JSON.stringify({
          status,
          reviewer: reviewerId,
          comment,
        }),
      }
    )
  },
}