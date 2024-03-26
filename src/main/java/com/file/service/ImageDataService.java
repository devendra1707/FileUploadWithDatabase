package com.file.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.file.entity.ImageData;
import com.file.entity.ImageUploadResponse;
import com.file.entity.ImageUtil;
import com.file.repo.ImageDataRepository;

import jakarta.transaction.Transactional;

@Service
public class ImageDataService {

	@Autowired
	private ImageDataRepository imageDataRepository;

	public ImageUploadResponse uploadImage(MultipartFile file) throws IOException {

		imageDataRepository.save(ImageData.builder().name(file.getOriginalFilename()).type(file.getContentType())
				.imageData(ImageUtil.compressImage(file.getBytes())).build());

		return new ImageUploadResponse("Image uploaded successfully: " + file.getOriginalFilename());

	}

	@Transactional
	public ImageData getInfoByImageByName(String name) {
		Optional<ImageData> dbImage = imageDataRepository.findByName(name);

		return ImageData.builder().name(dbImage.get().getName()).type(dbImage.get().getType())
				.imageData(ImageUtil.decompressImage(dbImage.get().getImageData())).build();

	}

	@Transactional
	public byte[] getImage(Long id) {
		Optional<ImageData> dbImage = imageDataRepository.findById(id);
		byte[] image = ImageUtil.decompressImage(dbImage.get().getImageData());
		System.out.println("----------------" + ImageUtil.decompressImage(dbImage.get().getImageData()));
		System.out.println("----------" + dbImage.get().getImageData());
		System.out.println();
//		System.out.println("==============" + dbImage.toString());
		return image;
	}

}