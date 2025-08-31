import React, { useEffect, useState } from "react";
import "./GreetingsList.css";

interface Greeting {
  id: number;
  message: string;
  createdAt: string; // Syncing with the API model from the handoff doc
}

// It's a good practice to store the API base URL in a constant.
// For production apps, this would typically come from an environment variable.
const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

const GreetingsList: React.FC = () => {
  const [greetings, setGreetings] = useState<Greeting[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [newMessage, setNewMessage] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [editingGreetingId, setEditingGreetingId] = useState<number | null>(
    null
  );
  const [editingText, setEditingText] = useState("");
  const [isUpdating, setIsUpdating] = useState(false);

  const fetchGreetings = async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch(`${API_BASE_URL}/greetings`);
      if (!res.ok) {
        throw new Error(
          `Failed to fetch greetings: ${res.status} ${res.statusText}`
        );
      }
      const data = await res.json();
      // Sort by creation date, newest first, for a consistent order.
      data.sort(
        (a: Greeting, b: Greeting) =>
          new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
      );
      setGreetings(data);
    } catch (err) {
      setError(
        err instanceof Error ? err.message : "An unknown error occurred"
      );
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchGreetings();
  }, []);

  const createGreeting = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newMessage.trim()) return; // Don't submit empty messages

    setIsSubmitting(true);
    try {
      const res = await fetch(`${API_BASE_URL}/greetings`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ message: newMessage }),
      });

      if (!res.ok) {
        throw new Error(
          `Failed to create greeting: ${res.status} ${res.statusText}`
        );
      }
      // Use the API response to add the new greeting for a faster UI update.
      const newGreeting: Greeting = await res.json();
      setGreetings((prev) => [newGreeting, ...prev]); // Prepend to the list
      setNewMessage(""); // Clear input
    } catch (err) {
      setError(
        err instanceof Error ? err.message : "An unknown error occurred"
      );
      console.error(err);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleUpdateSubmit = async (e: React.FormEvent, greetingId: number) => {
    e.preventDefault();
    if (!editingText.trim()) return;

    setIsUpdating(true);
    try {
      const res = await fetch(`${API_BASE_URL}/greetings/${greetingId}`, {
        method: "PUT", // Assuming PUT for updates as per REST conventions
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ message: editingText }),
      });

      if (!res.ok) {
        throw new Error(
          `Failed to update greeting: ${res.status} ${res.statusText}`
        );
      }

      const updatedGreeting = await res.json();
      setGreetings((prev) =>
        prev.map((g) => (g.id === greetingId ? updatedGreeting : g))
      );
      setEditingGreetingId(null);
    } catch (err) {
      setError(
        err instanceof Error ? err.message : "An unknown error occurred"
      );
      console.error(err);
    } finally {
      setIsUpdating(false);
    }
  };

  const deleteGreeting = async (id: number) => {
    try {
      // The API handoff specifies DELETE /greetings/{id}
      const res = await fetch(`${API_BASE_URL}/greetings/${id}`, {
        method: "DELETE",
      });
      if (!res.ok) {
        throw new Error(
          `Failed to delete greeting: ${res.status} ${res.statusText}`
        );
      }
      setGreetings((prev) => prev.filter((g) => g.id !== id));
    } catch (err) {
      setError(
        err instanceof Error ? err.message : "An unknown error occurred"
      );
      console.error(err);
      // For a better user experience, you could show this error in the UI,
      // for example, using a toast notification.
    }
  };

  const formatTimestamp = (isoString: string) => {
    return new Date(isoString).toLocaleDateString("en-US", {
      year: "numeric",
      month: "short",
      day: "numeric",
    });
  };

  if (loading) return <p>Loading greetings...</p>;

  if (error) return <p>Error: {error}</p>;

  return (
    <div className="greetingsPage">
      <h2 className="greetingsHeader">Greetings</h2>

      <form onSubmit={createGreeting} className="greetingForm">
        <input
          type="text"
          className="greetingInput"
          value={newMessage}
          onChange={(e) => setNewMessage(e.target.value)}
          placeholder="Add a new greeting..."
          disabled={isSubmitting}
        />
        <button
          type="submit"
          className="createButton"
          disabled={isSubmitting || !newMessage.trim()}
        >
          {isSubmitting ? "Adding..." : "Add Greeting"}
        </button>
      </form>

      <div className="greetingsContainer">
        {greetings.map((g) => (
          <div key={g.id} className="greetingCard">
            {editingGreetingId === g.id ? (
              <form
                onSubmit={(e) => handleUpdateSubmit(e, g.id)}
                className="cardEditForm"
              >
                <textarea
                  value={editingText}
                  onChange={(e) => setEditingText(e.target.value)}
                  className="cardEditTextarea"
                  disabled={isUpdating}
                  autoFocus
                />
                <div className="cardActions">
                  <button
                    type="button"
                    onClick={() => setEditingGreetingId(null)}
                    className="cancelButton"
                    disabled={isUpdating}
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    className="saveButton"
                    disabled={isUpdating || !editingText.trim()}
                  >
                    {isUpdating ? "Saving..." : "Save"}
                  </button>
                </div>
              </form>
            ) : (
              <>
                <p className="greetingMessage">{g.message}</p>
                <div className="cardFooter">
                  <span className="greetingTimestamp">
                    {formatTimestamp(g.createdAt)}
                  </span>
                  <div className="cardActions">
                    <button
                      onClick={() => {
                        setEditingGreetingId(g.id);
                        setEditingText(g.message);
                      }}
                      className="editButton"
                    >
                      Edit
                    </button>
                    <button
                      onClick={() => deleteGreeting(g.id)}
                      className="deleteButton"
                    >
                      Delete
                    </button>
                  </div>
                </div>
              </>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default GreetingsList;
