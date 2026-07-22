import { useEffect, useState } from 'react'
import { employeeApi } from '../api/employeeApi'
import type {
  Approval,
  Expense,
  User,
} from '../types/models'

interface ExpenseTableProps {
  expenses: Expense[]
  user: User
  onChanged: () => void
}

export default function ExpenseTable({
  expenses,
  user,
  onChanged,
}: ExpenseTableProps) {
  const [approvals, setApprovals] =
    useState<Record<number, Approval>>({})

  const [loadingId, setLoadingId] =
    useState<number | null>(null)

  useEffect(() => {
    async function loadApprovals() {
      const approvalEntries =
        await Promise.all(
          expenses.map(async (expense) => {
            try {
              const approval =
                await employeeApi.getApprovalByExpenseId(
                  expense.id
                )

              return [
                expense.id,
                approval,
              ] as const
            } catch {
              return null
            }
          })
        )

      const approvalMap: Record<
        number,
        Approval
      > = {}

      for (const entry of approvalEntries) {
        if (entry) {
          approvalMap[entry[0]] = entry[1]
        }
      }

      setApprovals(approvalMap)
    }

    loadApprovals()
  }, [expenses])

  async function handleDelete(
    expense: Expense
  ) {
    const approval =
      approvals[expense.id]

    if (approval?.status !== 'pending') {
      return
    }

    const confirmed = window.confirm(
      'Are you sure you want to delete this expense?'
    )

    if (!confirmed) {
      return
    }

    try {
      setLoadingId(expense.id)

      await employeeApi.deleteExpense(
        expense.id
      )

      onChanged()
    } catch (error) {
      console.error(error)
      alert(
        'Failed to delete expense.'
      )
    } finally {
      setLoadingId(null)
    }
  }

  async function handleEdit(
    expense: Expense
  ) {
    const approval =
      approvals[expense.id]

    if (approval?.status !== 'pending') {
      return
    }

    const amountInput =
      window.prompt(
        'Enter the new amount:',
        String(expense.amount)
      )

    if (amountInput === null) {
      return
    }

    const amount =
      Number(amountInput)

    if (
      Number.isNaN(amount) ||
      amount <= 0
    ) {
      alert(
        'Please enter a valid amount.'
      )

      return
    }

    const description =
      window.prompt(
        'Enter the new description:',
        expense.description
      )

    if (description === null) {
      return
    }

    const date =
      window.prompt(
        'Enter the new date (YYYY-MM-DD):',
        expense.date
      )

    if (date === null) {
      return
    }

    try {
      setLoadingId(expense.id)

      await employeeApi.updateExpense(
        expense.id,
        {
          user_id: user.id,
          amount,
          description,
          date,
        }
      )

      onChanged()
    } catch (error) {
      console.error(error)
      alert(
        'Failed to update expense.'
      )
    } finally {
      setLoadingId(null)
    }
  }

  if (expenses.length === 0) {
    return (
      <div className="rounded-2xl border border-slate-200 bg-white p-8 text-center text-slate-500 shadow-sm">
        No expenses found.
      </div>
    )
  }

  return (
    <div className="overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-sm">
      <div className="overflow-x-auto">
        <table className="w-full text-left text-sm">
          <thead className="border-b border-slate-200 bg-slate-50">
            <tr>
              <th className="px-6 py-4">
                ID
              </th>

              <th className="px-6 py-4">
                Amount
              </th>

              <th className="px-6 py-4">
                Description
              </th>

              <th className="px-6 py-4">
                Date
              </th>

              <th className="px-6 py-4">
                Status
              </th>

              <th className="px-6 py-4">
                Actions
              </th>
            </tr>
          </thead>

          <tbody>
            {expenses.map((expense) => {
              const approval =
                approvals[expense.id]

              const isPending =
                approval?.status ===
                'pending'

              const isLoading =
                loadingId === expense.id

              return (
                <tr
                  key={expense.id}
                  className="border-b border-slate-100 last:border-0"
                >
                  <td className="px-6 py-4 font-medium text-slate-900">
                    {expense.id}
                  </td>

                  <td className="px-6 py-4 font-semibold text-slate-900">
                    ${expense.amount}
                  </td>

                  <td className="px-6 py-4 text-slate-700">
                    {expense.description}
                  </td>

                  <td className="px-6 py-4 text-slate-700">
                    {expense.date}
                  </td>

                  <td className="px-6 py-4">
                    <span
                      className={`rounded-full px-3 py-1 text-xs font-semibold ${
                        approval?.status ===
                        'approved'
                          ? 'bg-green-100 text-green-700'
                          : approval?.status ===
                              'rejected'
                            ? 'bg-red-100 text-red-700'
                            : 'bg-yellow-100 text-yellow-700'
                      }`}
                    >
                      {approval?.status
                        ?.toUpperCase() ??
                        'LOADING'}
                    </span>
                  </td>

                  <td className="px-6 py-4">
                    {isPending ? (
                      <div className="flex gap-2">
                        <button
                          disabled={isLoading}
                          onClick={() =>
                            handleEdit(
                              expense
                            )
                          }
                          className="rounded-lg border border-slate-300 px-3 py-2 text-xs font-semibold text-slate-700 hover:bg-slate-50 disabled:opacity-40"
                        >
                          Edit
                        </button>

                        <button
                          disabled={isLoading}
                          onClick={() =>
                            handleDelete(
                              expense
                            )
                          }
                          className="rounded-lg bg-red-600 px-3 py-2 text-xs font-semibold text-white hover:bg-red-700 disabled:opacity-40"
                        >
                          Delete
                        </button>
                      </div>
                    ) : (
                      <span className="text-xs text-slate-400">
                        No actions available
                      </span>
                    )}
                  </td>
                </tr>
              )
            })}
          </tbody>
        </table>
      </div>
    </div>
  )
}