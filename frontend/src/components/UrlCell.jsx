export default function UrlCell({ url }) {
  if (!url) return '—'
  return (
    <a
      href={url}
      target="_blank"
      rel="noopener noreferrer"
      className="url-cell"
    >
      {url}
    </a>
  )
}
