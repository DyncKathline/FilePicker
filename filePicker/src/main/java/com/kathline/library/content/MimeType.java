package com.kathline.library.content;


import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({MimeType.TYPE_video, MimeType.TYPE_audio, MimeType.TYPE_image, MimeType.TYPE_3g2, MimeType.TYPE_asf, MimeType.TYPE_avi, MimeType.TYPE_flv, MimeType.TYPE_m4u, MimeType.TYPE_m4v, MimeType.TYPE_mov, MimeType.TYPE_mp4, MimeType.TYPE_mpe, MimeType.TYPE_mpeg, MimeType.TYPE_mpg, MimeType.TYPE_mpg4, MimeType.TYPE_ogv, MimeType.TYPE_webm, MimeType.TYPE_3gp, MimeType.TYPE_aac, MimeType.TYPE_flac, MimeType.TYPE_m3u, MimeType.TYPE_m4a, MimeType.TYPE_m4b, MimeType.TYPE_m4p, MimeType.TYPE_mid, MimeType.TYPE_midi, MimeType.TYPE_mp2, MimeType.TYPE_mp3, MimeType.TYPE_mpga, MimeType.TYPE_oga, MimeType.TYPE_ogg, MimeType.TYPE_rmvb, MimeType.TYPE_wav, MimeType.TYPE_weba, MimeType.TYPE_wma, MimeType.TYPE_wmv, MimeType.TYPE_bmp, MimeType.TYPE_gif, MimeType.TYPE_ico, MimeType.TYPE_jpeg, MimeType.TYPE_jpg, MimeType.TYPE_png, MimeType.TYPE_svg, MimeType.TYPE_tif, MimeType.TYPE_tiff, MimeType.TYPE_webp, MimeType.TYPE_c, MimeType.TYPE_cpp, MimeType.TYPE_h, MimeType.TYPE_conf, MimeType.TYPE_css, MimeType.TYPE_csv, MimeType.TYPE_gradle, MimeType.TYPE_htm, MimeType.TYPE_html, MimeType.TYPE_htmls, MimeType.TYPE_ics, MimeType.TYPE_iml, MimeType.TYPE_java, MimeType.TYPE_js, MimeType.TYPE_log, MimeType.TYPE_mjs, MimeType.TYPE_prop, MimeType.TYPE_rc, MimeType.TYPE_txt, MimeType.TYPE_xml, MimeType.TYPE_otf, MimeType.TYPE_ttf, MimeType.TYPE_woff, MimeType.TYPE_woff2, MimeType.TYPE_7z, MimeType.TYPE_abw, MimeType.TYPE_apk, MimeType.TYPE_arc, MimeType.TYPE_azw, MimeType.TYPE_bin, MimeType.TYPE_bz, MimeType.TYPE_bz2, MimeType.TYPE_class, MimeType.TYPE_csh, MimeType.TYPE_doc, MimeType.TYPE_docx, MimeType.TYPE_eot, MimeType.TYPE_epub, MimeType.TYPE_exe, MimeType.TYPE_gtar, MimeType.TYPE_gz, MimeType.TYPE_gzip, MimeType.TYPE_hdf, MimeType.TYPE_help, MimeType.TYPE_jar, MimeType.TYPE_json, MimeType.TYPE_jsonld, MimeType.TYPE_m3u8, MimeType.TYPE_mpc, MimeType.TYPE_mpkg, MimeType.TYPE_msg, MimeType.TYPE_odp, MimeType.TYPE_ods, MimeType.TYPE_odt, MimeType.TYPE_ogx, MimeType.TYPE_pdf, MimeType.TYPE_pps, MimeType.TYPE_ppt, MimeType.TYPE_pptx, MimeType.TYPE_rar, MimeType.TYPE_rtf, MimeType.TYPE_sh, MimeType.TYPE_swf, MimeType.TYPE_tar, MimeType.TYPE_tgz, MimeType.TYPE_vsd, MimeType.TYPE_wps, MimeType.TYPE_xhtml, MimeType.TYPE_xlc, MimeType.TYPE_xls, MimeType.TYPE_xlsx, MimeType.TYPE_xul, MimeType.TYPE_zip})
public @interface MimeType {
     String TYPE_video = "video/*";
    String TYPE_audio = "audio/*";
    String TYPE_image = "image/*";

    String TYPE_3g2 = "video/3gpp2";
    String TYPE_asf = "video/x-ms-asf";
    String TYPE_avi = "video/x-msvideo";
    String TYPE_flv = "video/x-flv";
    String TYPE_m4u = "video/vnd.mpegurl";
    String TYPE_m4v = "video/x-m4v";
    String TYPE_mov = "video/quicktime";
    String TYPE_mp4 = "video/mp4";
    String TYPE_mpe = "video/mpeg";
    String TYPE_mpeg = "video/mpeg";
    String TYPE_mpg = "video/mpeg";
    String TYPE_mpg4 = "video/mp4";
    String TYPE_ogv = "video/ogg";
    String TYPE_webm = "video/webm";

    String TYPE_3gp = "audio/3gpp";
    String TYPE_aac = "audio/aac";
    String TYPE_flac = "audio/*";
    String TYPE_m3u = "audio/x-mpegurl";
    String TYPE_m4a = "audio/mp4a-latm";
    String TYPE_m4b = "audio/mp4a-latm";
    String TYPE_m4p = "audio/mp4a-latm";
    String TYPE_mid = "audio/midi audio/x-midi";
    String TYPE_midi = "audio/midi audio/x-midi";
    String TYPE_mp2 = "audio/x-mpeg";
    String TYPE_mp3 = "audio/x-mpeg";
    String TYPE_mpga = "audio/mpeg";
    String TYPE_oga = "audio/ogg";
    String TYPE_ogg = "audio/ogg";
    String TYPE_rmvb = "audio/x-pn-realaudio";
    String TYPE_wav = "audio/wav";
    String TYPE_weba = "audio/webm";
    String TYPE_wma = "audio/x-ms-wma";
    String TYPE_wmv = "audio/x-ms-wmv";

    String TYPE_bmp = "image/bmp";
    String TYPE_gif = "image/gif";
    String TYPE_ico = "image/vnd.microsoft.icon";
    String TYPE_jpg = "image/jpg";
    String TYPE_jpeg = "image/jpeg";
    String TYPE_png = "image/png";
    String TYPE_svg = "image/svg+xml";
    String TYPE_tif = "image/tiff";
    String TYPE_tiff = "image/tiff";
    String TYPE_webp = "image/webp";

    String TYPE_c = "text/plain";
    String TYPE_cpp = "text/x-c";
    String TYPE_h = "text/plain";
    String TYPE_conf = "text/plain";
    String TYPE_css = "text/css";
    String TYPE_csv = "text/csv";
    String TYPE_gradle = "text/plain";
    String TYPE_htm = "text/html";
    String TYPE_html = "text/html";
    String TYPE_htmls = "text/html";
    String TYPE_ics = "text/calendar";
    String TYPE_iml = "text/plain";
    String TYPE_java = "text/x-java-source";
    String TYPE_js = "text/javascript";
    String TYPE_log = "text/plain";
    String TYPE_mjs = "text/javascript";
    String TYPE_prop = "text/plain";
    String TYPE_rc = "text/plain";
    String TYPE_txt = "text/plain";
    String TYPE_xml = "text/xml";

    String TYPE_otf = "font/otf";
    String TYPE_ttf = "font/ttf";
    String TYPE_woff = "font/woff";
    String TYPE_woff2 = "font/woff2";

    String TYPE_7z = "application/x-7z-compressed";
    String TYPE_abw = "application/x-abiword";
    String TYPE_apk = "application/vnd.android.package-archive";
    String TYPE_arc = "application/x-freearc";
    String TYPE_azw = "application/vnd.amazon.ebook";
    String TYPE_bin = "application/octet-stream";
    String TYPE_bz = "application/x-bzip";
    String TYPE_bz2 = "application/x-bzip2";
    String TYPE_class = "application/octet-stream";
    String TYPE_csh = "application/x-csh";
    String TYPE_doc = "application/msword";
    String TYPE_docx = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    String TYPE_eot = "application/vnd.ms-fontobject";
    String TYPE_epub = "application/epub+zip";
    String TYPE_exe = "application/octet-stream";
    String TYPE_gtar = "application/x-gtar";
    String TYPE_gz = "application/x-gzip";
    String TYPE_gzip = "application/x-gzip";
    String TYPE_hdf = "application/x-hdf";
    String TYPE_help = "application/x-helpfile";
    String TYPE_jar = "application/java-archive";
    String TYPE_json = "application/json";
    String TYPE_jsonld = "application/ld+json";
    String TYPE_m3u8 = "application/ld+json";
    String TYPE_mpc = "application/vnd.mpohun.certificate";
    String TYPE_mpkg = "application/vnd.apple.installer+xml";
    String TYPE_msg = "application/vnd.ms-outlook";
    String TYPE_odp = "application/vnd.oasis.opendocument.presentation";
    String TYPE_ods = "application/vnd.oasis.opendocument.spreadsheet";
    String TYPE_odt = "application/vnd.oasis.opendocument.text";
    String TYPE_ogx = "application/ogg";
    String TYPE_pdf = "application/pdf";
    String TYPE_pps = "application/vnd.ms-powerpoint";
    String TYPE_ppt = "application/vnd.ms-powerpoint";
    String TYPE_pptx = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
    String TYPE_rar = "application/x-rar-compressed";
    String TYPE_rtf = "application/rtf";
    String TYPE_sh = "application/x-sh";
    String TYPE_swf = "application/x-shockwave-flash";
    String TYPE_tar = "application/x-tar";
    String TYPE_tgz = "application/x-compressed";
    String TYPE_vsd = "application/vnd.visio";
    String TYPE_wps = "application/vnd.ms-works";
    String TYPE_xhtml = "application/xhtml+xml";
    String TYPE_xlc = "application/excel";
    String TYPE_xls = "application/vnd.ms-excel";
    String TYPE_xlsx = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    String TYPE_xul = "application/vnd.mozilla.xul+xml";
    String TYPE_zip = "application/zip";
}