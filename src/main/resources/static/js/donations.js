// Track used locations
let usedLocations = new Set();

const API_ENDPOINTS = {
    STAFF_INFO: '/api/staff/current',
    CATEGORIES: '/api/categories',
    SUBCATEGORIES: '/api/subcategories',
    CHECK_LOCATION: '/api/check-location',
    SUBMIT_DONATION: '/api/donations'
};

// Load staff name
async function loadStaffName() {
    try {
        const response = await fetch(API_ENDPOINTS.STAFF_INFO);
        const data = await response.json();
        document.getElementById('staffName').textContent = data.name || 'Staff';
    } catch (error) {
        console.error('Failed to load staff name:', error);
        document.getElementById('staffName').textContent = 'Staff';
    }
}

// Load categories from database
async function loadCategories() {
    try {
        const response = await fetch(API_ENDPOINTS.CATEGORIES);
        const categories = await response.json();

        const select = document.querySelector('select[name="mainCategory"]');
        select.innerHTML = '<option value="">Select Category</option>';

        const uniqueCategories = [...new Set(categories.map(cat => cat.mainCategory))];
        uniqueCategories.forEach(category => {
            const option = new Option(category, category);
            select.add(option);
        });
    } catch (error) {
        console.error('Failed to load categories:', error);
    }
}

// Load subcategories
async function loadSubcategories() {
    const mainCategory = document.querySelector('select[name="mainCategory"]').value;

    try {
        const response = await fetch(`${API_ENDPOINTS.SUBCATEGORIES}/${mainCategory}`);
        const subcategories = await response.json();

        const select = document.querySelector('select[name="subCategory"]');
        select.innerHTML = '<option value="">Select Sub Category</option>';

        subcategories.forEach(subcat => {
            const option = new Option(subcat.subCategory, subcat.subCategory);
            select.add(option);
        });
    } catch (error) {
        console.error('Failed to load subcategories:', error);
    }
}

// Image preview functionality
function previewImage(input) {
    const preview = document.getElementById('imagePreview');
    preview.innerHTML = '';

    if (input.files && input.files[0]) {
        const file = input.files[0];

        // Validate file size (5MB max)
        if (file.size > 5 * 1024 * 1024) {
            alert('Image must be less than 5MB');
            input.value = '';
            preview.innerHTML = '<span class="placeholder-text">No image selected</span>';
            return;
        }

        const reader = new FileReader();
        reader.onload = function (e) {
            preview.innerHTML = `
                <img src="${e.target.result}" alt="Item preview">
                <button type="button" class="button remove-image" onclick="removeImage()">
                    <i class="fas fa-times"></i>
                </button>
            `;
        };
        reader.readAsDataURL(file);
    } else {
        preview.innerHTML = '<span class="placeholder-text">No image selected</span>';
    }
}

function removeImage() {
    document.getElementById('itemImage').value = '';
    document.getElementById('imagePreview').innerHTML =
        '<span class="placeholder-text">No image selected</span>';
}

function createPieceFormHTML(index) {
    return `
        <div class="piece-container" id="piece-${index}">
            <h4>Piece #${index + 1}</h4>
            
            <div class="form-group">
                <label>Description</label>
                <input type="text" name="piece_description_${index}" required>
            </div>

            <div class="form-group">
                <label>Dimensions (inches)</label>
                <div class="grid-3">
                    <input type="number" name="piece_length_${index}" placeholder="Length" required min="1">
                    <input type="number" name="piece_width_${index}" placeholder="Width" required min="1">
                    <input type="number" name="piece_height_${index}" placeholder="Height" required min="1">
                </div>
            </div>

            <div class="form-group">
                <label>Location *</label>
                <div class="grid-2">
                    <input type="number" 
                           name="piece_room_${index}" 
                           placeholder="Room Number" 
                           required 
                           min="1"
                           onchange="checkLocationAvailability(${index})">
                    <input type="number" 
                           name="piece_shelf_${index}" 
                           placeholder="Shelf Number" 
                           required 
                           min="1"
                           onchange="checkLocationAvailability(${index})">
                </div>
                <div class="location-status" id="locationStatus_${index}"></div>
            </div>

            <div class="form-group">
                <label>Notes</label>
                <textarea name="piece_notes_${index}"></textarea>
            </div>

            ${index > 0 ? `
                <button type="button" class="button" onclick="removePiece(${index})">
                    Remove Piece
                </button>
            ` : ''}
        </div>
    `;
}

async function checkLocationAvailability(index) {
    const roomInput = document.querySelector(`[name="piece_room_${index}"]`);
    const shelfInput = document.querySelector(`[name="piece_shelf_${index}"]`);
    const statusDiv = document.getElementById(`locationStatus_${index}`);

    const room = roomInput.value;
    const shelf = shelfInput.value;

    if (!room || !shelf) return;

    const locationKey = `${room}-${shelf}`;

    try {
        if (usedLocations.has(locationKey)) {
            statusDiv.textContent = "Location already assigned to another piece";
            statusDiv.className = "location-status unavailable";
            return false;
        }

        const response = await fetch(`${API_ENDPOINTS.CHECK_LOCATION}?room=${room}&shelf=${shelf}`);
        const data = await response.json();

        if (data.available) {
            statusDiv.textContent = "Location available";
            statusDiv.className = "location-status available";
            usedLocations.add(locationKey);
            return true;
        } else {
            statusDiv.textContent = "Location unavailable";
            statusDiv.className = "location-status unavailable";
            return false;
        }
    } catch (error) {
        statusDiv.textContent = "Error checking location";
        statusDiv.className = "location-status unavailable";
        return false;
    }
}

function togglePiecesSection() {
    const hasPieces = document.getElementById('hasPieces').checked;
    const piecesSection = document.getElementById('piecesSection');

    piecesSection.style.display = hasPieces ? 'block' : 'none';

    if (hasPieces && !document.querySelector('.piece-container')) {
        addPieceForm();
    }
}

function addPieceForm() {
    const container = document.getElementById('piecesContainer');
    const index = container.children.length;

    const pieceForm = document.createElement('div');
    pieceForm.innerHTML = createPieceFormHTML(index);
    container.appendChild(pieceForm);
}

function removePiece(index) {
    const piece = document.getElementById(`piece-${index}`);

    const room = piece.querySelector(`[name="piece_room_${index}"]`).value;
    const shelf = piece.querySelector(`[name="piece_shelf_${index}"]`).value;
    usedLocations.delete(`${room}-${shelf}`);

    piece.remove();

    const pieces = document.querySelectorAll('.piece-container');
    pieces.forEach((piece, idx) => {
        piece.querySelector('h4').textContent = `Piece #${idx + 1}`;
    });
}

async function handleSubmit(event) {
    event.preventDefault();

    if (document.getElementById('hasPieces').checked) {
        const pieces = document.querySelectorAll('.piece-container');
        for (let piece of pieces) {
            const statusDiv = piece.querySelector('.location-status');
            if (!statusDiv.classList.contains('available')) {
                alert('Please ensure all piece locations are valid');
                return;
            }
        }
    }

    const formData = new FormData(event.target);

    // Add file to FormData if exists
    const imageFile = document.getElementById('itemImage').files[0];
    if (imageFile) {
        formData.append('itemImage', imageFile);
    }

    try {
        const response = await fetch(API_ENDPOINTS.SUBMIT_DONATION, {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            alert('Donation recorded successfully');
            window.location.href = 'dashboard.html';
        } else {
            alert('Failed to record donation');
        }
    } catch (error) {
        alert('Error recording donation');
        console.error(error);
    }
}

document.addEventListener('DOMContentLoaded', async () => {
    await Promise.all([
        loadStaffName(),
        loadCategories()
    ]);
    togglePiecesSection();
});