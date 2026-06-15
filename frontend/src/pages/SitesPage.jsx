import { Link } from 'react-router-dom'
import { useApi } from '../hooks/useApi.js'
import { api } from '../api/client.js'
import LoadingState from '../components/LoadingState.jsx'
import EmptyState from '../components/EmptyState.jsx'
import ErrorState from '../components/ErrorState.jsx'

export default function SitesPage() {
  const { data: sites, loading, error } = useApi(api.getSites, [])

  return (
    <div className="page-wrapper">
      <nav className="breadcrumbs">
        <span>Home</span>
      </nav>
      <h1 className="page-title">Sites</h1>

      {loading && <LoadingState />}
      {error   && <ErrorState message={error} />}

      {!loading && !error && sites?.length === 0 && (
        <EmptyState message="No sites configured" />
      )}

      {!loading && !error && sites?.length > 0 && (
        <div className="sites-grid">
          {sites.map(site => (
            <Link key={site.id} to={`/sites/${site.id}`} className="site-card">
              <div>
                <div className="site-name">{site.name}</div>
                <div className="site-url">{site.url}</div>
              </div>
              <span className="chevron">›</span>
            </Link>
          ))}
        </div>
      )}
    </div>
  )
}
