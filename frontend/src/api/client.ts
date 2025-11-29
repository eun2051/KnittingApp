// API 클라이언트 설정
const BASE_URL = 'http://localhost:8080/api'

class ApiClient {
  private async request<T>(url: string, options: RequestInit = {}): Promise<T> {
    const token = localStorage.getItem('jwt');
    const response = await fetch(`${BASE_URL}${url}`, {
      headers: {
        'Content-Type': 'application/json',
        ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
        ...options.headers,
      },
      ...options,
    })

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    return response.json()
  }

  // GET 요청
  async get<T>(url: string): Promise<T> {
    return this.request<T>(url)
  }

  // POST 요청
  async post<T>(url: string, data?: any): Promise<T> {
    return this.request<T>(url, {
      method: 'POST',
      body: data ? JSON.stringify(data) : undefined,
    })
  }

  // PUT 요청
  async put<T>(url: string, data?: any): Promise<T> {
    return this.request<T>(url, {
      method: 'PUT',
      body: data ? JSON.stringify(data) : undefined,
    })
  }

  // DELETE 요청
  async delete<T>(url: string): Promise<T> {
    return this.request<T>(url, {
      method: 'DELETE',
    })
  }
}

export const apiClient = new ApiClient()