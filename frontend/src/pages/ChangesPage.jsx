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
  { key: 'url',        label: 'URL',         render: v => <UrlCell url={v} /> },
  { key: 'changeType', label: 'Change Type' },
  { key: 'oldValue',   label: 'Old Value' },
  { key: 'newValue',   label: 'New Value' },
  { key: 'checkedAt',  label: 'Checked At',  render: v => formatDate(v) },
]

export default function ChangesPage() {
  const { siteId } = useOutletContext()
  const { data, loading, error } = useApi(
    () => api.getChanges(siteId),
    [siteId]
  )

  if (loading) return <LoadingState />
  if (error)   return <ErrorState message={error} />

  const rows = data?.content

  return (
    <div>
      <h1 className="page-title">Changes</h1>
      {!rows?.length
        ? <EmptyState message="No changes detected" />
        : <DataTable columns={COLUMNS} data={rows} />
      }
    </div>
  )
}
