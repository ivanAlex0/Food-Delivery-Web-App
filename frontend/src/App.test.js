import { render, screen } from '@testing-library/react';
import AdminLogin from './pages/admin/AdminLogin';

test('renders learn react link', () => {
  render(<AdminLogin />);
  const linkElement = screen.getByText(/learn react/i);
  expect(linkElement).toBeInTheDocument();
});
