import { useParams, Outlet } from 'react-router-dom'
import Breadcrumbs from '../components/Breadcrumbs.jsx'
import { useApi } from '../hooks/useApi.js'
import { api } from '../api/client.js'

export default function SiteLayout() {
  const { siteId } = useParams()
  const { data: site } = useApi(() => api.getSiteById(siteId), [siteId])

  return (
    <div className="page-wrapper">
      <Breadcrumbs site={site} />
      <Outlet context={{ site, siteId }} />
    </div>
  )
}
