import { useState } from 'react'
import { useOutletContext } from 'react-router-dom'
import { useApi } from '../hooks/useApi.js'
import { api } from '../api/client.js'
import { formatDate } from '../utils.js'
import DataTable from '../components/DataTable.jsx'
import Pagination from '../components/Pagination.jsx'
import UrlCell from '../components/UrlCell.jsx'
import LoadingState from '../components/LoadingState.jsx'
import EmptyState from '../components/EmptyState.jsx'
import ErrorState from '../components/ErrorState.jsx'

const COLUMNS = [
  { key: 'pageId',        label: 'Page ID' },
  { key: 'url',           label: 'URL',          render: v => <UrlCell url={v} /> },
  { key: 'currentH1',     label: 'Current H1' },
  { key: 'h1Exists',      label: 'H1 Exists',    render: v => (
    <span className={`badge ${v ? 'badge-yes' : 'badge-no'}`}>{v ? 'Yes' : 'No'}</span>
  )},
  { key: 'createdAt',     label: 'Created At',   render: v => formatDate(v) },
  { key: 'lastCheckedAt', label: 'Last Checked', render: v => formatDate(v) },
]

export default function PagesPage() {
  const { siteId } = useOutletContext()
  const [page, setPage] = useState(0)

  const { data, loading, error } = useApi(
    () => api.getPages(siteId, page),
    [siteId, page]
  )

  if (loading) return <LoadingState />
  if (error)   return <ErrorState message={error} />

  const rows = data?.content ?? []

  return (
    <div>
      <h1 className="page-title">Pages</h1>
      {rows.length === 0 ? (
        <EmptyState message="No pages found" />
      ) : (
        <DataTable
          columns={COLUMNS}
          data={rows}
          footer={
            <Pagination
              page={page}
              totalPages={data?.totalPages ?? 1}
              onPageChange={setPage}
            />
          }
        />
      )}
    </div>
  )
}
