import { useEffect, useState } from 'react'
import { managerApi } from '../api/managerApi'
import type {
    Approval,
  Expense,
  Review,
  User,
} from '../types/models'
import { ReviewModal } from './ReviewModal'
import { DecisionModal } from './DecisionModal'

interface ManagerDashboardProps {
  user: User
  onLogout: () => void
}

type StatusFilter =
  | 'ALL'
  | 'PENDING'
  | 'APPROVED'
  | 'DENIED'

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
    
    const [approvals, setApprovals] =
            useState<Record<number, Approval>>({})

    const [expenseToReview, setExpenseToReview] =
            useState<Review | null> (null)

    const [currentPage, setCurrentPage] = useState(1)

    const expensesPerPage = 10

    const totalPages = Math.ceil(
    expenses.length / expensesPerPage
    )

    const startIndex =
    (currentPage - 1) * expensesPerPage

    const currentExpenses = expenses.slice(
    startIndex,
    startIndex + expensesPerPage
    )

    const handleExitReview = () => {
        setExpenseToReview(null)
    }

    const handleSubmitDecision = async (expenseId: number, status: string, reviewId: number, comment: string) => {
        if(expenseToReview){
            managerApi.updateApprovalStatus(expenseId, status, reviewId, comment)
        }
        approvals[expenseId].status = status
        handleExitReview()
    }

    const handleReview = (expense: Expense, approval: Approval) => {
        const review = {
                approval_id: approval.id,
                user_id: expense.user_id,
                expense_id: expense.id,
                amount: expense.amount,
                description: expense.description,
                date: expense.date,
                status: approval.status,
                reviewer: approval.reviewer,
                comment: approval.comment,
                review_date: approval.review_date
        }

        setExpenseToReview(review)
    }

    async function loadApprovals(
    initialExpenses: Expense[]
    ): Promise<Record<number, Approval>> {
    const approvalEntries = await Promise.all(
        initialExpenses.map(async (expense) => {
        try {
            const approval =
            await managerApi.getApprovalByExpenseId(
                expense.id
            )

            if (!approval) {
            return null
            }

            return [
            expense.id,
            approval,
            ] as const
        } catch {
            return null
        }
        })
    )

    const approvalMap: Record<number, Approval> = {}

    for (const entry of approvalEntries) {
        if (entry) {
        approvalMap[entry[0]] = entry[1]
        }
    }

    setApprovals(approvalMap)

    return approvalMap
    }

  async function loadExpenses() {
    try {
      setLoading(true)
        
      const unfilteredData = await managerApi.findAllExpenses()
      const approvals = await loadApprovals(unfilteredData)

      const data = filter === 'ALL' ? unfilteredData : unfilteredData.filter(e => approvals[e.id]?.status.toUpperCase() == filter)
      setExpenses(data.sort((a, b)=>b.id - a.id))
    } catch (error) {
      console.error(error)
      setExpenses([])
    } finally {
      setLoading(false)
    }
  }

    useEffect(() => {
        setCurrentPage(1)
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
              'DENIED',
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
                        Status
                    </th>

                  <th className="px-6 py-4">
                    Actions
                  </th>
                </tr>
              </thead>

              <tbody>
                {currentExpenses.map((expense) => {
                    const approval =
                        approvals[expense.id]

                    return (
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
                            <span
                                className={`rounded-full px-3 py-1 text-xs font-semibold ${
                                    approval?.status ===
                                    'approved'
                                        ? 'bg-green-100 text-green-700'
                                        : approval?.status ===
                                            'denied'
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
                        <button
                            onClick={()=>handleReview(expense, approval)}
                            className="rounded-lg border border-slate-300 px-3 py-2 text-xs font-semibold text-slate-700 hover:bg-slate-50 disabled:opacity-40"
                        >
                            Review
                        </button>
                        </td>
                    </tr>
                )})}
              </tbody>
            </table>
          )}
          <div className="flex items-center justify-between border-t border-slate-200 px-6 py-4">
            <p className="text-sm text-slate-500">
                Page {currentPage} of {totalPages}
            </p>

            <div className="flex gap-2">
                <button
                onClick={() =>
                    setCurrentPage((page) => page - 1)
                }
                disabled={currentPage === 1}
                className="rounded-lg border border-slate-300 px-3 py-2 text-sm disabled:cursor-not-allowed disabled:opacity-50"
                >
                Previous
                </button>

                <button
                onClick={() =>
                    setCurrentPage((page) => page + 1)
                }
                disabled={currentPage === totalPages}
                className="rounded-lg border border-slate-300 px-3 py-2 text-sm disabled:cursor-not-allowed disabled:opacity-50"
                >
                Next
                </button>
            </div>
            </div>
        </div>
        {expenseToReview && (expenseToReview.status.toLocaleUpperCase() === "PENDING" ? 
        <DecisionModal reviewerID={user.id} approval={approvals[expenseToReview.expense_id]} onClose={handleExitReview} onSubmit={handleSubmitDecision}/> : 
        <ReviewModal isManager={true} review={expenseToReview} onClose={handleExitReview}/>)}
      </main>
    </div>
  )
}

