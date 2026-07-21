import { useState } from 'react'
import Login from './components/Login'
import ManagerDashboard from './components/ManagerDashboard'
import EmployeeDashboard from './components/EmployeeDashboard'
import type { User } from './types/models'

function App() {
  const [user, setUser] = useState<User | null>(null)

  if (!user) {
    return <Login onLogin={setUser} />
  }

    if (user.role === 'Manager') {
    return (
      <ManagerDashboard
        user={user}
        onLogout={() => setUser(null)}
      />
    )
  }

  return (
    <EmployeeDashboard
      user={user}
      onLogout={() => setUser(null)}
    />
  )
}

export default App