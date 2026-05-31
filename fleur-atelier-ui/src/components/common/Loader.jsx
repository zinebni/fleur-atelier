export default function Loader({ fullscreen = false, size = 40 }) {
  if (fullscreen) {
    return (
      <div className="loader-fullscreen">
        <div className="loader-ring" style={{ width: size, height: size }} />
      </div>
    );
  }
  return <div className="loader-ring" style={{ width: size, height: size }} />;
}
