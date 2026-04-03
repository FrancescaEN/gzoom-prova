export class FileUtils {
  static downloadFile(response: any): void {
    const contentType = response.headers.get('content-type');
    const blob = new Blob([response.body], {type: contentType});
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');

    // Extracting filename from headers
    const contentDisposition = response.headers.get('Content-Disposition');
    let fileName = 'download';
    if (contentDisposition) {
      const matches = /filename="?([^"]+)"?/.exec(contentDisposition);
      if (matches != null && matches[1]) fileName = matches[1];
    }

    link.href = url;
    link.download = fileName || 'download';
    link.click();
    URL.revokeObjectURL(url);
  }
}
