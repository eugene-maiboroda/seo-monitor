import { Link, useLocation } from 'react-router-dom'

const SECTION_LABELS = {
  pages:     'Pages',
  'no-h1':   'No H1',
  redirects: 'Redirects',
  errors:    'Errors',
  changes:   'Changes',
}

export default function Breadcrumbs({ site }) {
  const { pathname } = useLocation()
  const parts = pathname.split('/').filter(Boolean)
  const lastPart = parts[parts.length - 1]
  const section = SECTION_LABELS[lastPart] ?? null

  return (
    <nav className="breadcrumbs">
      <Link to="/">Home</Link>
      {site && (
        <>
          <span className="sep">/</span>
          {section
            ? <Link to={`/sites/${site.id}`}>{site.name}</Link>
            : <span>{site.name}</span>
          }
        </>
      )}
      {section && (
        <>
          <span className="sep">/</span>
          <span>{section}</span>
        </>
      )}
    </nav>
  )
}
