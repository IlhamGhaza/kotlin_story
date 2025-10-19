# 📸 Dicoding Story API

API untuk berbagi cerita seputar Dicoding, mirip seperti post Instagram, tetapi khusus untuk komunitas Dicoding.

> **Base URL:** `https://story-api.dicoding.dev/v1`

---

## Authentication

### Register

* **URL:** `/register`
* **Method:** `POST`
* **Request Body:**

  ```json
  {
    "name": "string",
    "email": "string (unique)",
    "password": "string (min. 8 characters)"
  }
  ```
* **Response:**

  ```json
  {
    "error": false,
    "message": "User Created"
  }
  ```

### Login

* **URL:** `/login`
* **Method:** `POST`
* **Request Body:**

  ```json
  {
    "email": "string",
    "password": "string"
  }
  ```
* **Response:**

  ```json
  {
    "error": false,
    "message": "success",
    "loginResult": {
      "userId": "string",
      "name": "string",
      "token": "string"
    }
  }
  ```

---

## Add New Story

### With Authentication

* **URL:** `/stories`

* **Method:** `POST`

* **Headers:**

  ```
  Authorization: Bearer <token>
  Content-Type: multipart/form-data
  ```

* **Request Body:**

  * `description` (string)
  * `photo` (file, image, max 1MB)
  * `lat` (float, optional)
  * `lon` (float, optional)

* **Response:**

  ```json
  {
    "error": false,
    "message": "success"
  }
  ```

### Guest (No Authentication)

* **URL:** `/stories/guest`

* **Method:** `POST`

* **Request Body:**

  * `description` (string)
  * `photo` (file, image, max 1MB)
  * `lat` (float, optional)
  * `lon` (float, optional)

* **Response:**

  ```json
  {
    "error": false,
    "message": "success"
  }
  ```

---

## Get Stories

### All Stories

* **URL:** `/stories`

* **Method:** `GET`

* **Headers:**

  ```
  Authorization: Bearer <token>
  ```

* **Query Parameters (Optional):**

  * `page`: integer
  * `size`: integer
  * `location`: `1` (with location) or `0` (default: without location)

* **Response:**

  ```json
  {
    "error": false,
    "message": "Stories fetched successfully",
    "listStory": [
      {
        "id": "string",
        "name": "string",
        "description": "string",
        "photoUrl": "url",
        "createdAt": "datetime",
        "lat": float,
        "lon": float
      }
    ]
  }
  ```

### Detail Story

* **URL:** `/stories/:id`
* **Method:** `GET`
* **Headers:**

  ```
  Authorization: Bearer <token>
  ```
* **Response:**

  ```json
  {
    "error": false,
    "message": "Story fetched successfully",
    "story": {
      "id": "string",
      "name": "string",
      "description": "string",
      "photoUrl": "url",
      "createdAt": "datetime",
      "lat": float,
      "lon": float
    }
  }
  ```

---

## Web Push Notification

### VAPID Public Key

```
BCCs2eonMI-6H2ctvFaWg-UYdDv387Vno_bzUzALpB442r2lCnsHmtrx8biyPi_E-1fSGABK_Qs_GlvPoJJqxbk
```

### Notification Schema

```json
{
  "title": "Story berhasil dibuat",
  "options": {
    "body": "Anda telah membuat story baru dengan deskripsi: <story description>"
  }
}
```

### Subscribe

* **URL:** `/notifications/subscribe`

* **Method:** `POST`

* **Headers:**

  ```
  Authorization: Bearer <token>
  Content-Type: application/json
  ```

* **Request Body:**

  ```json
  {
    "endpoint": "string",
    "keys": {
      "p256dh": "string",
      "auth": "string"
    }
  }
  ```

* **Response:**

  ```json
  {
    "error": false,
    "message": "Success to subscribe web push notification.",
    "data": {
      "id": "string",
      "endpoint": "string",
      "keys": {
        "p256dh": "string",
        "auth": "string"
      },
      "userId": "string",
      "createdAt": "datetime"
    }
  }
  ```

### Unsubscribe

* **URL:** `/notifications/subscribe`

* **Method:** `DELETE`

* **Headers:**

  ```
  Authorization: Bearer <token>
  Content-Type: application/json
  ```

* **Request Body:**

  ```json
  {
    "endpoint": "string"
  }
  ```

* **Response:**

  ```json
  {
    "error": false,
    "message": "Success to unsubscribe web push notification."
  }
  ```

---
