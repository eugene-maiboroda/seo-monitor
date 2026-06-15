export default function ErrorState({ message = 'Something went wrong' }) {
  return (
    <div className="state-container state-error">
      <div className="state-title">Failed to load data</div>
      <div className="state-msg">{message}</div>
    </div>
  )
}
