/**
 * @file Modelos de banners de inicio y subida de medios.
 * @description Tipos para el carrusel de la home y respuestas del panel admin.
 * @see {@link ../services/home-banner.service.ts} Listado público activo.
 * @see {@link ../services/admin.service.ts} CRUD y subida de imágenes.
 */

/** Banner promocional mostrado en la página de inicio. */
export interface HomeBanner {
  id: number;
  imageUrl: string;
  title?: string | null;
  subtitle?: string | null;
  active: boolean;
  displayOrder: number;
}

/** Metadatos de un archivo subido vía `POST /admin/media/images`. */
export interface UploadedImage {
  url: string;
  fileName: string;
  contentType: string;
  size: number;
}
