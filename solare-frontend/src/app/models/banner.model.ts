export interface HomeBanner {
  id: number;
  imageUrl: string;
  title?: string | null;
  subtitle?: string | null;
  active: boolean;
  displayOrder: number;
}

export interface UploadedImage {
  url: string;
  fileName: string;
  contentType: string;
  size: number;
}
