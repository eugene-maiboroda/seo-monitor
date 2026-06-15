import { Link } from 'react-router-dom'

export default function StatCard({ label, value, to }) {
  const body = (
    <>
      <div className="label">{label}</div>
      <div className="value">{value ?? '—'}</div>
    </>
  )

  if (to) {
    return <Link to={to} className="stat-card clickable">{body}</Link>
  }
  return <div className="stat-card">{body}</div>
}
