export default function EmptyState({ message = 'No data found' }) {
  return (
    <div className="state-container">
      <div className="state-title">{message}</div>
    </div>
  )
}
