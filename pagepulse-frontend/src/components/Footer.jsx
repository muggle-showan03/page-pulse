import '../styles/Footer.css';

function Footer() {
  return (
    <footer className="footer">
      <p className="footer__text">
        Built for{' '}
        <a
          href="https://digitalheroesco.com"
          target="_blank"
          rel="noopener noreferrer"
          className="footer__link"
        >
          Digital Heroes Training Task
        </a>
      </p>
    </footer>
  );
}

export default Footer;
