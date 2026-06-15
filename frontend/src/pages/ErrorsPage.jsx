import { useOutletContext } from 'react-router-dom'
import { useApi } from '../hooks/useApi.js'
import { api } from '../api/client.js'
import { formatDate } from '../utils.js'
import DataTable from '../components/DataTable.jsx'
import UrlCell from '../components/UrlCell.jsx'
import LoadingState from '../components/LoadingState.jsx'
import EmptyState from '../components/EmptyState.jsx'
import ErrorState from '../components/ErrorState.jsx'

const COLUMNS = [
  { key: 'url',          label: 'URL',          render: v => <UrlCell url={v} /> },
  { key: 'errorMessage', label: 'Error Message' },
  { key: 'checkedAt',    label: 'Checked At',   render: v => formatDate(v) },
]

export default function ErrorsPage() {
  const { siteId } = useOutletContext()
  const { data: rows, loading, error } = useApi(
    () => api.getErrors(siteId),
    [siteId]
  )

  if (loading) return <LoadingState />
  if (error)   return <ErrorState message={error} />

  return (
    <div>
      <h1 className="page-title">Errors</h1>
      {!rows?.length
        ? <EmptyState message="No errors found" />
        : <DataTable columns={COLUMNS} data={rows} />
      }
    </div>
  )
}
