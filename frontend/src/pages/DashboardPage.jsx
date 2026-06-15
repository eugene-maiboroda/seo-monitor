import { useOutletContext } from 'react-router-dom'
import { useApi } from '../hooks/useApi.js'
import { api } from '../api/client.js'
import { formatDate } from '../utils.js'
import StatCard from '../components/StatCard.jsx'
import LoadingState from '../components/LoadingState.jsx'
import ErrorState from '../components/ErrorState.jsx'

export default function DashboardPage() {
  const { siteId } = useOutletContext()
  const { data: summary, loading, error } = useApi(
    () => api.getSummary(siteId),
    [siteId]
  )

  if (loading) return <LoadingState />
  if (error)   return <ErrorState message={error} />

  return (
    <div>
      <h1 className="page-title">Dashboard</h1>
      <div className="stat-grid">
        <StatCard label="Pages"      value={summary?.pages}       to="pages" />
        <StatCard label="No H1"      value={summary?.noH1Page}    to="no-h1" />
        <StatCard label="Redirects"  value={summary?.redirectPage} to="redirects" />
        <StatCard label="Errors"     value={summary?.errorPage}   to="errors" />
        <StatCard label="Changes"    value={summary?.changesPage}  to="changes" />
        <StatCard label="Last Audit" value={formatDate(summary?.lastAudit)} />
      </div>
    </div>
  )
}
