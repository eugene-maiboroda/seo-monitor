import { BrowserRouter, Routes, Route, Link } from 'react-router-dom'
import SitesPage from './pages/SitesPage.jsx'
import SiteLayout from './pages/SiteLayout.jsx'
import DashboardPage from './pages/DashboardPage.jsx'
import PagesPage from './pages/PagesPage.jsx'
import NoH1Page from './pages/NoH1Page.jsx'
import RedirectsPage from './pages/RedirectsPage.jsx'
import ErrorsPage from './pages/ErrorsPage.jsx'
import ChangesPage from './pages/ChangesPage.jsx'

function AppHeader() {
  return (
    <header className="app-header">
      <Link to="/" className="logo">
        SEO <span>Monitor</span>
      </Link>
    </header>
  )
}

export default function App() {
  return (
    <BrowserRouter basename="/api">
      <AppHeader />
      <Routes>
        <Route path="/" element={<SitesPage />} />
        <Route path="/sites/:siteId" element={<SiteLayout />}>
          <Route index element={<DashboardPage />} />
          <Route path="pages"     element={<PagesPage />} />
          <Route path="no-h1"     element={<NoH1Page />} />
          <Route path="redirects" element={<RedirectsPage />} />
          <Route path="errors"    element={<ErrorsPage />} />
          <Route path="changes"   element={<ChangesPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}
