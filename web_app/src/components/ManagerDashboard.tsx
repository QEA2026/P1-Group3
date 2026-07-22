import { useEffect, useState } from 'react'
import { managerApi } from '../api/managerApi'
import type {
  Expense,
  User,
} from '../types/models'

interface ManagerDashboardProps {
  user: User
  onLogout: () => void
}

type StatusFilter =
  | 'ALL'
  | 'PENDING'
  | 'APPROVED'
  | 'REJECTED'

export default function ManagerDashboard({
  user,
  onLogout,
}: ManagerDashboardProps) {
  const [expenses, setExpenses] =
    useState<Expense[]>([])

  const [filter, setFilter] =
    useState<StatusFilter>('ALL')

  const [loading, setLoading] =
    useState(true)

  async function loadExpenses() {
    try {
      setLoading(true)
        
      const unfilteredData = await managerApi.findAllExpenses()
      const data = filter === 'ALL' ? unfilteredData : unfilteredData.filter(e => true)

      setExpenses(data)
    } catch (error) {
      console.error(error)
      setExpenses([])
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadExpenses()
  }, [filter])

  return (
    <div className="min-h-screen bg-slate-100">
      <header className="border-b border-slate-200 bg-white">
        <div className="mx-auto flex max-w-7xl items-center justify-between px-6 py-5">
          <div>
            <h1 className="text-xl font-bold text-slate-900">
              Manager Expense Portal
            </h1>

            <p className="text-sm text-slate-500">
              Welcome back, {user.username}
            </p>
          </div>

          <button
            onClick={onLogout}
            className="rounded-lg border border-slate-300 px-4 py-2 text-sm font-semibold text-slate-700 hover:bg-slate-50"
          >
            Logout
          </button>
        </div>
      </header>

      <main className="mx-auto max-w-7xl px-6 py-8">
        <div className="mb-8">
          <h2 className="text-3xl font-bold text-slate-900">
            Expense Review
          </h2>

          <p className="mt-2 text-slate-500">
            Review and manage employee expense reports.
          </p>
        </div>

        <div className="mb-6 grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
          <div className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
            <p className="text-sm text-slate-500">
              Total Expenses
            </p>

            <p className="mt-2 text-3xl font-bold">
              {expenses.length}
            </p>
          </div>
        </div>

        <div className="mb-6 flex flex-wrap gap-2 rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
          {(
            [
              'ALL',
              'PENDING',
              'APPROVED',
              'REJECTED',
            ] as StatusFilter[]
          ).map((status) => (
            <button
              key={status}
              onClick={() => setFilter(status)}
              className={`rounded-lg px-4 py-2.5 text-sm font-semibold ${
                filter === status
                  ? 'bg-slate-900 text-white'
                  : 'border border-slate-300 text-slate-700 hover:bg-slate-50'
              }`}
            >
              {status}
            </button>
          ))}
        </div>

        <div className="overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-sm">
          {loading ? (
            <div className="p-8 text-center text-slate-500">
              Loading expenses...
            </div>
          ) : expenses.length === 0 ? (
            <div className="p-8 text-center text-slate-500">
              No expenses found.
            </div>
          ) : (
            <table className="w-full text-left text-sm">
              <thead className="border-b border-slate-200 bg-slate-50">
                <tr>
                  <th className="px-6 py-4">
                    ID
                  </th>

                  <th className="px-6 py-4">
                    Employee ID
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
                    Actions
                  </th>
                </tr>
              </thead>

              <tbody>
                {expenses.map((expense) => (
                  <tr
                    key={expense.id}
                    className="border-b border-slate-100"
                  >
                    <td className="px-6 py-4">
                      {expense.id}
                    </td>

                    <td className="px-6 py-4">
                      {expense.user_id}
                    </td>

                    <td className="px-6 py-4 font-semibold">
                      ${expense.amount}
                    </td>

                    <td className="px-6 py-4">
                      {expense.description}
                    </td>

                    <td className="px-6 py-4">
                      {expense.date}
                    </td>

                    <td className="px-6 py-4">
                      <button className="rounded-lg bg-blue-600 px-3 py-2 text-xs font-semibold text-white hover:bg-blue-700">
                        Review
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </main>
    </div>
  )
}