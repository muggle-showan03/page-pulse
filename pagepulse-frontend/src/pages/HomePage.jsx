import { useState } from 'react';
import Header from '../components/Header';
import UrlInput from '../components/UrlInput';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorAlert from '../components/ErrorAlert';
import ResultCards from '../components/ResultCards';
import Footer from '../components/Footer';
import { analyzeUrl } from '../services/api';
import '../styles/HomePage.css';

function HomePage() {
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleAnalyze = async (url) => {
    setError('');
    setResult(null);
    setIsLoading(true);

    try {
      const data = await analyzeUrl(url);
      setResult(data);
    } catch (err) {
      const message =
        err.response?.data?.message ||
        err.message ||
        'An unexpected error occurred. Please try again.';
      setError(message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="home-page">
      <Header />
      <UrlInput onAnalyze={handleAnalyze} isLoading={isLoading} />
      <ErrorAlert message={error} onDismiss={() => setError('')} />
      {isLoading && <LoadingSpinner />}
      {!isLoading && result && <ResultCards data={result} />}
      <Footer />
    </div>
  );
}

export default HomePage;
